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

  const truthCanvas   = root.querySelector('[data-sn-canvas="truth"]');
  const samplerCanvas = root.querySelector('[data-sn-canvas="sampler"]');
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
    updateReadout();
  };
  intervalInput.addEventListener("input", onInput);
  windowInput.addEventListener("input", onInput);
  onInput();

  // -- Animation loop ------------------------------------------------------
  const tick = () => {
    drawTruth();
    drawSampler();
    requestAnimationFrame(tick);
  };
  requestAnimationFrame(tick);

  // -- Resize handling -----------------------------------------------------
  let resizeTimer = null;
  window.addEventListener("resize", () => {
    if (resizeTimer) cancelAnimationFrame(resizeTimer);
    resizeTimer = requestAnimationFrame(() => { drawTruth(); drawSampler(); });
  });
})();
