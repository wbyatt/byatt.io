(() => {
  "use strict";

  // -- Simulation parameters (the "physical" pulse train) ------------------
  const PULSE_AMPL_GiB   = 1;     // peak amplitude
  const PULSE_PERIOD_S   = 44;    // period of pulse train
  const PULSE_WIDTH_S    = 3;     // width of each pulse
  const TIME_WINDOW_S    = 360;   // visible window, simulated seconds
  const SIM_SPEED        = 4;     // simulated seconds per real second

  // -- Theme: read from CSS custom properties on :root ---------------------
  const cssVar = (name) =>
    getComputedStyle(document.documentElement).getPropertyValue(name).trim();

  const COLOR = {
    bg:       cssVar("--paper"),
    bg2:      cssVar("--paper-2"),
    rule:     cssVar("--rule"),
    fg:       cssVar("--ink"),
    fg2:      cssVar("--ink-2"),
    fg3:      cssVar("--ink-3"),
    truth:    cssVar("--brass"),
    sampler:  cssVar("--cordovan"),
    ghost:    cssVar("--ink-4"),
  };
  const FONT_MONO = cssVar("--font-mono") || "JetBrains Mono, monospace";

  // -- DOM lookup ----------------------------------------------------------
  const root = document.querySelector(".sn-rig");
  if (!root) return;

  const truthCanvas    = root.querySelector('[data-sn-canvas="truth"]');
  const samplerCanvas  = root.querySelector('[data-sn-canvas="sampler"]');
  const spectrumCanvas = root.querySelector('[data-sn-canvas="spectrum"]');
  const intervalInput = root.querySelector('[data-sn-input="interval"]');
  const windowInput   = root.querySelector('[data-sn-input="window"]');
  const intervalOut   = root.querySelector('[data-sn-output="interval"]');
  const windowOut     = root.querySelector('[data-sn-output="window"]');
  const readout       = root.querySelector('[data-sn-readout]');

  // -- DPI-aware sizing ----------------------------------------------------
  const fitCanvas = (canvas) => {
    const dpr = window.devicePixelRatio || 1;
    const rect = canvas.getBoundingClientRect();
    canvas.width  = Math.round(rect.width  * dpr);
    canvas.height = Math.round(rect.height * dpr);
    const ctx = canvas.getContext("2d");
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
    return { ctx, w: rect.width, h: rect.height };
  };

  // -- Signal: 1 GiB pulse train, instantaneous edges ----------------------
  const signal = (t) => {
    const phase = ((t % PULSE_PERIOD_S) + PULSE_PERIOD_S) % PULSE_PERIOD_S;
    return phase < PULSE_WIDTH_S ? PULSE_AMPL_GiB : 0;
  };

  // -- Windowed integral of signal over [t - w, t] -------------------------
  // For w = 0 this collapses to point-sampling.
  // Closed-form: the pulse train is periodic, so the integral is
  // determined by how many full pulses and how much partial pulse the
  // window covers. We compute it numerically in 0.05 s steps for
  // simplicity — fast enough at ~20 samples per integration window.
  const windowSample = (t, w) => {
    if (w <= 0) return signal(t);
    const STEP = 0.05;
    const n = Math.max(1, Math.round(w / STEP));
    let sum = 0;
    for (let i = 0; i < n; i++) {
      const tt = t - w + (i + 0.5) * (w / n);
      sum += signal(tt);
    }
    return sum / n;
  };

  // -- FFT (Cooley-Tukey radix-2, in-place) --------------------------------
  // re/im are equal-length Float64Arrays whose length is a power of 2.
  const fft = (re, im) => {
    const N = re.length;
    // bit-reverse permutation
    for (let i = 0, j = 0; i < N; i++) {
      if (i < j) {
        let t = re[i]; re[i] = re[j]; re[j] = t;
        t     = im[i]; im[i] = im[j]; im[j] = t;
      }
      let m = N >> 1;
      while (m >= 1 && j >= m) { j -= m; m >>= 1; }
      j += m;
    }
    for (let size = 2; size <= N; size <<= 1) {
      const half = size >> 1;
      const baseAngle = -2 * Math.PI / size;
      for (let start = 0; start < N; start += size) {
        for (let k = 0; k < half; k++) {
          const a = baseAngle * k;
          const c = Math.cos(a), s = Math.sin(a);
          const i1 = start + k, i2 = start + k + half;
          const tre = c * re[i2] - s * im[i2];
          const tim = s * re[i2] + c * im[i2];
          re[i2] = re[i1] - tre;
          im[i2] = im[i1] - tim;
          re[i1] += tre;
          im[i1] += tim;
        }
      }
    }
  };

  const nextPow2 = (n) => {
    let p = 1;
    while (p < n) p <<= 1;
    return p;
  };

  // Compute one-sided amplitude spectrum of a real-valued sample array.
  // Returns { freqs: Float64Array, amps: Float64Array } covering [0, fs/2].
  const spectrum = (samples, sampleSpacing) => {
    const N = samples.length;
    const re = new Float64Array(N);
    const im = new Float64Array(N);
    for (let i = 0; i < N; i++) re[i] = samples[i];
    fft(re, im);
    const half = N >> 1;
    const freqs = new Float64Array(half);
    const amps  = new Float64Array(half);
    const fs = 1 / sampleSpacing;
    for (let k = 0; k < half; k++) {
      freqs[k] = k * fs / N;
      amps[k]  = (2 / N) * Math.hypot(re[k], im[k]);
    }
    return { freqs, amps };
  };

  // -- Cached spectra: deterministic functions of (interval, window) -------
  const ANALYSIS_WINDOW_S = 2048;       // total simulated seconds per FFT
  const TRUTH_DT          = 0.5;        // truth sample spacing (s)
  const TRUTH_N           = nextPow2(ANALYSIS_WINDOW_S / TRUTH_DT);

  const computeTruthSpectrum = () => {
    const samples = new Float64Array(TRUTH_N);
    for (let i = 0; i < TRUTH_N; i++) samples[i] = signal(i * TRUTH_DT);
    return spectrum(samples, TRUTH_DT);
  };

  const computeSamplerSpectrum = (interval, win) => {
    // Use the largest power of 2 that fits in ANALYSIS_WINDOW_S at this rate.
    let N = 1;
    while ((N << 1) * interval <= ANALYSIS_WINDOW_S) N <<= 1;
    if (N < 8) N = 8;
    const samples = new Float64Array(N);
    for (let i = 0; i < N; i++) samples[i] = windowSample(i * interval, win);
    return spectrum(samples, interval);
  };

  const truthSpectrum = computeTruthSpectrum();   // constant
  let   samplerSpectrum;                          // recomputed on slider change

  // -- State ---------------------------------------------------------------
  const state = {
    interval: parseFloat(intervalInput.value),
    window:   parseFloat(windowInput.value),
    simStart: performance.now(),  // real ms when sim t=0 was anchored
  };

  const simTime = () =>
    ((performance.now() - state.simStart) / 1000) * SIM_SPEED;

  // -- Drawing helpers -----------------------------------------------------
  const PAD = { top: 24, right: 28, bottom: 28, left: 80 };

  const plotRect = (w, h) => ({
    x: PAD.left,
    y: PAD.top,
    w: w - PAD.left - PAD.right,
    h: h - PAD.top - PAD.bottom,
  });

  const drawFrame = (ctx, w, h, label) => {
    ctx.clearRect(0, 0, w, h);
    ctx.fillStyle = COLOR.bg;
    ctx.fillRect(0, 0, w, h);

    const r = plotRect(w, h);

    // Y gridlines: 0, 0.25, 0.5, 0.75, 1.0 GiB
    ctx.font = `11px ${FONT_MONO}`;
    ctx.textBaseline = "middle";
    ctx.textAlign = "right";
    ctx.fillStyle = COLOR.fg3;
    for (let i = 0; i <= 4; i++) {
      const v = i / 4;
      const py = r.y + r.h * (1 - v);
      ctx.strokeStyle = COLOR.rule;
      ctx.beginPath();
      ctx.moveTo(r.x, py);
      ctx.lineTo(r.x + r.w, py);
      ctx.stroke();
      ctx.fillText(`${v.toFixed(2)} GiB`, r.x - 8, py);
    }

    // X gridlines: every 60s back from "now"
    ctx.textAlign = "center";
    ctx.textBaseline = "top";
    const tNow = simTime();
    const tStart = tNow - TIME_WINDOW_S;
    for (let s = Math.ceil(tStart / 60) * 60; s <= tNow; s += 60) {
      const px = r.x + ((s - tStart) / TIME_WINDOW_S) * r.w;
      ctx.strokeStyle = COLOR.rule;
      ctx.beginPath();
      ctx.moveTo(px, r.y);
      ctx.lineTo(px, r.y + r.h);
      ctx.stroke();
      const ago = Math.round(tNow - s);
      ctx.fillStyle = COLOR.fg3;
      ctx.fillText(`-${ago}s`, px, r.y + r.h + 8);
    }

    // Frame
    ctx.strokeStyle = COLOR.rule;
    ctx.strokeRect(r.x, r.y, r.w, r.h);

    return { r, tNow, tStart };
  };

  const drawTruth = () => {
    const { ctx, w, h } = fitCanvas(truthCanvas);
    const { r, tNow, tStart } = drawFrame(ctx, w, h);

    // Draw signal as filled-step area + line.
    ctx.fillStyle = "rgba(168, 138, 58, 0.22)";  // brass at low alpha
    ctx.strokeStyle = COLOR.truth;
    ctx.lineWidth = 1.5;

    ctx.beginPath();
    let started = false;
    const STEP = 0.25; // s, dense enough to render square edges crisply
    for (let t = tStart; t <= tNow; t += STEP) {
      const v = signal(t);
      const px = r.x + ((t - tStart) / TIME_WINDOW_S) * r.w;
      const py = r.y + r.h * (1 - v / PULSE_AMPL_GiB);
      if (!started) { ctx.moveTo(px, py); started = true; }
      else          { ctx.lineTo(px, py); }
    }
    ctx.stroke();

    // Filled area beneath
    ctx.lineTo(r.x + r.w, r.y + r.h);
    ctx.lineTo(r.x, r.y + r.h);
    ctx.closePath();
    ctx.fill();
  };

  const drawSampler = () => {
    const { ctx, w, h } = fitCanvas(samplerCanvas);
    const { r, tNow, tStart } = drawFrame(ctx, w, h);

    // Clip to plot rect so the trace and sample dots can't bleed outside
    // the chart area as samples scroll in from the right or off the left.
    ctx.save();
    ctx.beginPath();
    ctx.rect(r.x, r.y, r.w, r.h);
    ctx.clip();

    // Sampler trace: step interpolation between scrape points.
    // Sample at fixed wall-clock-aligned multiples of `interval` so the
    // pattern is reproducible between frames.
    const interval = state.interval;
    const win = state.window;

    const firstSample = Math.floor(tStart / interval) * interval;
    const samples = [];
    for (let s = firstSample; s <= tNow + interval; s += interval) {
      const px = r.x + ((s - tStart) / TIME_WINDOW_S) * r.w;
      const v  = windowSample(s, win);
      const py = r.y + r.h * (1 - v / PULSE_AMPL_GiB);
      samples.push({ t: s, v, px, py });
    }

    // Filled area under the step trace.
    if (samples.length > 0) {
      ctx.fillStyle = "rgba(110, 42, 30, 0.18)";  // cordovan at low alpha
      ctx.beginPath();
      ctx.moveTo(samples[0].px, r.y + r.h);
      ctx.lineTo(samples[0].px, samples[0].py);
      for (let i = 1; i < samples.length; i++) {
        ctx.lineTo(samples[i].px, samples[i - 1].py);
        ctx.lineTo(samples[i].px, samples[i].py);
      }
      ctx.lineTo(samples[samples.length - 1].px, r.y + r.h);
      ctx.closePath();
      ctx.fill();
    }

    // Step-interpolated stroke on top of the fill.
    ctx.strokeStyle = COLOR.sampler;
    ctx.lineWidth = 1.75;
    ctx.beginPath();
    for (let i = 0; i < samples.length; i++) {
      if (i === 0) {
        ctx.moveTo(samples[i].px, samples[i].py);
      } else {
        ctx.lineTo(samples[i].px, samples[i - 1].py);
        ctx.lineTo(samples[i].px, samples[i].py);
      }
    }
    ctx.stroke();

    // Sample dots on top
    ctx.fillStyle = COLOR.sampler;
    for (const s of samples) {
      if (s.t < tStart || s.t > tNow) continue;
      ctx.beginPath();
      ctx.arc(s.px, s.py, 2.5, 0, Math.PI * 2);
      ctx.fill();
    }

    ctx.restore();
  };

  // -- Spectrum panel ------------------------------------------------------
  const FREQ_MAX = 0.10;  // Hz, x-axis range
  const FREQ_PAD = { top: 24, right: 28, bottom: 28, left: 80 };

  const drawSpectrum = () => {
    const { ctx, w, h } = fitCanvas(spectrumCanvas);
    const r = {
      x: FREQ_PAD.left,
      y: FREQ_PAD.top,
      w: w - FREQ_PAD.left - FREQ_PAD.right,
      h: h - FREQ_PAD.top - FREQ_PAD.bottom,
    };

    ctx.clearRect(0, 0, w, h);
    ctx.fillStyle = COLOR.bg;
    ctx.fillRect(0, 0, w, h);

    // Find amplitude scale across both spectra in the visible band.
    let ampMax = 0;
    const collect = (sp) => {
      for (let i = 0; i < sp.freqs.length; i++) {
        if (sp.freqs[i] > FREQ_MAX) break;
        if (sp.amps[i] > ampMax) ampMax = sp.amps[i];
      }
    };
    collect(truthSpectrum);
    collect(samplerSpectrum);
    if (ampMax === 0) ampMax = 1;
    // Round up to a clean tick value.
    const ampTick = Math.pow(10, Math.floor(Math.log10(ampMax)));
    const ampScale = Math.ceil(ampMax / ampTick) * ampTick;

    // Y gridlines / labels (5 levels).
    ctx.font = `11px ${FONT_MONO}`;
    ctx.textBaseline = "middle";
    ctx.textAlign = "right";
    ctx.fillStyle = COLOR.fg3;
    for (let i = 0; i <= 4; i++) {
      const v = (i / 4) * ampScale;
      const py = r.y + r.h * (1 - i / 4);
      ctx.strokeStyle = COLOR.rule;
      ctx.beginPath();
      ctx.moveTo(r.x, py);
      ctx.lineTo(r.x + r.w, py);
      ctx.stroke();
      ctx.fillText(v.toExponential(1), r.x - 8, py);
    }

    // X gridlines / labels every 0.02 Hz.
    ctx.textAlign = "center";
    ctx.textBaseline = "top";
    for (let f = 0; f <= FREQ_MAX + 1e-9; f += 0.02) {
      const px = r.x + (f / FREQ_MAX) * r.w;
      ctx.strokeStyle = COLOR.rule;
      ctx.beginPath();
      ctx.moveTo(px, r.y);
      ctx.lineTo(px, r.y + r.h);
      ctx.stroke();
      ctx.fillStyle = COLOR.fg3;
      ctx.fillText(f.toFixed(2) + " Hz", px, r.y + r.h + 8);
    }

    // Frame.
    ctx.strokeStyle = COLOR.rule;
    ctx.strokeRect(r.x, r.y, r.w, r.h);

    // Clip plot area for traces.
    ctx.save();
    ctx.beginPath();
    ctx.rect(r.x, r.y, r.w, r.h);
    ctx.clip();

    const drawTrace = (sp, color, fillAlpha, fmax) => {
      if (!sp || sp.freqs.length === 0) return;
      const fillStr = color === COLOR.truth
        ? `rgba(168, 138, 58, ${fillAlpha})`
        : `rgba(110, 42, 30, ${fillAlpha})`;

      // Build path of the spectrum curve.
      ctx.beginPath();
      let started = false;
      for (let i = 0; i < sp.freqs.length; i++) {
        const f = sp.freqs[i];
        if (f > fmax) break;
        const px = r.x + (f / FREQ_MAX) * r.w;
        const py = r.y + r.h * (1 - sp.amps[i] / ampScale);
        if (!started) { ctx.moveTo(px, py); started = true; }
        else          { ctx.lineTo(px, py); }
      }
      // Close with bottom edge for fill.
      const lastFreq = Math.min(fmax, sp.freqs[sp.freqs.length - 1]);
      const lastX = r.x + (lastFreq / FREQ_MAX) * r.w;
      ctx.lineTo(lastX, r.y + r.h);
      ctx.lineTo(r.x, r.y + r.h);
      ctx.closePath();
      ctx.fillStyle = fillStr;
      ctx.fill();

      // Stroke pass: just the curve, no closing.
      ctx.beginPath();
      started = false;
      for (let i = 0; i < sp.freqs.length; i++) {
        const f = sp.freqs[i];
        if (f > fmax) break;
        const px = r.x + (f / FREQ_MAX) * r.w;
        const py = r.y + r.h * (1 - sp.amps[i] / ampScale);
        if (!started) { ctx.moveTo(px, py); started = true; }
        else          { ctx.lineTo(px, py); }
      }
      ctx.strokeStyle = color;
      ctx.lineWidth = 1.5;
      ctx.stroke();
    };

    drawTrace(truthSpectrum,   COLOR.truth,   0.18, FREQ_MAX);
    const fsHalf = 1 / (2 * state.interval);
    drawTrace(samplerSpectrum, COLOR.sampler, 0.22, fsHalf);

    ctx.restore();

    // Sampler-Nyquist marker line + label, drawn on top of the clip.
    if (fsHalf <= FREQ_MAX) {
      const nx = r.x + (fsHalf / FREQ_MAX) * r.w;
      ctx.strokeStyle = COLOR.sampler;
      ctx.setLineDash([4, 3]);
      ctx.beginPath();
      ctx.moveTo(nx, r.y);
      ctx.lineTo(nx, r.y + r.h);
      ctx.stroke();
      ctx.setLineDash([]);
      ctx.fillStyle = COLOR.sampler;
      ctx.textAlign = "left";
      ctx.textBaseline = "top";
      ctx.font = `10px ${FONT_MONO}`;
      ctx.fillText("fs/2", nx + 4, r.y + 4);
    }
  };

  // -- Readout: derived quantities -----------------------------------------
  const gcd = (a, b) => {
    a = Math.round(a); b = Math.round(b);
    while (b) { [a, b] = [b, a % b]; }
    return a;
  };
  const lcm = (a, b) => Math.round((a * b) / gcd(a, b));

  const updateReadout = () => {
    const fs = 1 / state.interval;        // sample frequency, Hz
    const fEvent = 1 / PULSE_PERIOD_S;    // pulse fundamental, Hz
    const nyquist = fs / 2;
    const aboveNyquist = fEvent > nyquist;

    // Alias period of the fundamental, if undersampled
    let aliasStr = "n/a (sampling above Nyquist)";
    if (aboveNyquist) {
      const fMod = ((fEvent % fs) + fs) % fs;
      const fAlias = fMod > fs / 2 ? fs - fMod : fMod;
      aliasStr = `${(1 / fAlias).toFixed(1)} s`;
    }

    const lcmS = lcm(state.interval, PULSE_PERIOD_S);
    const dutyCycle = PULSE_WIDTH_S / PULSE_PERIOD_S;
    const asymptote = (dutyCycle * PULSE_AMPL_GiB).toFixed(3);

    let mode;
    if (state.window === 0) {
      mode = "point sample";
    } else if (state.window < state.interval) {
      mode = `integrating (window &lt; interval)`;
    } else if (state.window === state.interval) {
      mode = `integrating (window = interval)`;
    } else {
      mode = `integrating (window &gt; interval, oversmoothed)`;
    }

    readout.innerHTML = `
      <strong>mode:</strong> ${mode} ·
      <strong>f<sub>s</sub></strong> = ${fs.toFixed(4)} Hz ·
      <strong>nyquist</strong> = ${nyquist.toFixed(4)} Hz ·
      <strong>pulse f</strong> = ${fEvent.toFixed(4)} Hz ${aboveNyquist ? "(undersampled)" : "(in band)"} ·
      <strong>alias period</strong> = ${aliasStr} ·
      <strong>LCM(scrape, pulse)</strong> = ${lcmS} s ·
      <strong>duty-cycle mean</strong> = ${asymptote} GiB
    `;
  };

  // -- Input handling ------------------------------------------------------
  const formatWindow = (w) =>
    w === 0 ? "0 s · point sample" : `${w} s`;

  const onInput = () => {
    state.interval = parseFloat(intervalInput.value);
    state.window   = parseFloat(windowInput.value);
    intervalOut.textContent = `${state.interval} s`;
    windowOut.textContent   = formatWindow(state.window);
    samplerSpectrum = computeSamplerSpectrum(state.interval, state.window);
    updateReadout();
  };
  intervalInput.addEventListener("input", onInput);
  windowInput.addEventListener("input", onInput);
  onInput();

  // -- Animation loop ------------------------------------------------------
  const tick = () => {
    drawTruth();
    drawSampler();
    drawSpectrum();
    requestAnimationFrame(tick);
  };
  requestAnimationFrame(tick);

  // -- Resize handling -----------------------------------------------------
  let resizeTimer = null;
  window.addEventListener("resize", () => {
    if (resizeTimer) cancelAnimationFrame(resizeTimer);
    resizeTimer = requestAnimationFrame(() => {
      drawTruth(); drawSampler(); drawSpectrum();
    });
  });
})();
