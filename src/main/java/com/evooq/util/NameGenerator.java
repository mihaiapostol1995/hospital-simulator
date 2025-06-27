package com.evooq.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NameGenerator {

    public static final List<String> AVAILABLE_NAMES = List.of(
            "Admiring Curie", "Agitated Einstein", "Brave Newton", "Compassionate Galileo", "Dazzling Tesla",
            "Determined Bohr", "Distracted Feynman", "Eager Kepler", "Elastic Faraday", "Epic Darwin",
            "Fervent Maxwell", "Focused Hawking", "Gallant Archimedes", "Gifted Euler", "Gracious Mendel",
            "Hardy Turing", "Happy Lovelace", "Hopeful Pascal", "Infallible Ptolemy", "Intelligent Shannon",
            "Jolly Copernicus", "Kind Gauss", "Laughing Planck", "Lively Descartes", "Loving Pythagoras",
            "Magical Boyle", "Modest Ampere", "Mystifying Dirac", "Nervous Heisenberg", "Nifty Laplace",
            "Optimistic Lorentz", "Pensive Raman", "Quizzical Boltzmann", "Radiant Noether", "Relaxed Fourier",
            "Reverent Carnot", "Romantic Cauchy", "Serene Babbage", "Sharp Landau", "Sleepy Euclid",
            "Stoic Legendre", "Strange Poisson", "Suspicious Eddington", "Tender Fibonacci", "Thirsty Bernoulli",
            "Upbeat Rutherford", "Vibrant Euler", "Vigorous Torricelli", "Witty Bernstein", "Zealous Hilbert"
    );
}
