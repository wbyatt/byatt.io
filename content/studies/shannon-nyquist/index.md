---
title: "Signals-Theoretic Observability"
subtitle: "aliasing in production telemetry, demonstrated"
date: 2026-05-07T12:00:00-04:00
draft: false
# rig:
#   - { key: "pulse period",      value: "44 s" }
#   - { key: "pulse width",       value: "3 s" }
#   - { key: "pulse amplitude",   value: "1 GiB" }
#   - { key: "scrape interval",   value: "1–60 s · adjustable" }
#   - { key: "integration window", value: "0–60 s · adjustable" }
---

A 1 GiB memory pulse, observed two ways: as the continuous signal it physically is, and through a sampler whose interval and integration window can vary. The same workload renders correctly under one set of sampler parameters and misleadingly under others.

{{< sn-interactive >}}

## The generator signal

A 1 GiB anonymous allocation held for 3 s, released, re-allocated 44 s later. Strictly periodic; instantaneous rise and fall; duty cycle 3/44 ≈ 6.8 %. The top panel renders this exactly.

## Reconstruction mathematics

The pulse train decomposes into a Fourier series with components at *f*₀, 2*f*₀, 3*f*₀, …, where *f*₀ = 1/44 Hz. The sampler's Nyquist frequency is *f*ₛ / 2 = 1 / (2 · interval); any harmonic above it folds into the baseband at |*f* mod *f*ₛ|. The integration window contributes a sinc-shaped low-pass envelope: at window = 0 every harmonic folds without attenuation, and as the window grows, higher-frequency content is suppressed *before* it can alias. LCM(interval, 44 s) sets the recurrence period of the visible pattern.

## Pathologies

**Bimodal LCM clustering** — point sample, interval 30 s. Over each 660-second LCM cycle the sampler catches the pulse exactly twice, 90 s apart, then runs silent for 9.5 minutes. The dashboard reads as a rare outlier rather than a 6.8 % duty-cycle steady state.

**Amplitude smearing** — integrating window between zero and the pulse period (try interval 30 s, window 10 s). Each scrape returns the mean over a partial pulse: values land between ~100 MiB and ~300 MiB but never reach 1 GiB. Hits look frequent; the dashboard reads as noise when the underlying signal is strictly binary.

**DC convergence** — integrating window much greater than the pulse period (try interval 30 s, window 60 s). The signal converges to its duty-cycle-weighted mean, ≈ 70 MiB. The pulse structure is gone entirely; the dashboard reads as a quiet steady-state load.
