import matplotlib.pyplot as plt

# Data (replace with actual experimental results)
population_sizes = [50, 100, 200]
mutation_rates = [0.01, 0.05, 0.1]
crossover_rates = [0.7, 0.8, 0.95]

# Example convergence rate data for each population size
convergence_data = {
    50: [
        [30, 28, 25],  # Mutation rate 0.01, for crossover rates 0.7, 0.8, 0.95
        [27, 26, 24],  # Mutation rate 0.05
        [25, 23, 22],  # Mutation rate 0.1
    ],
    100: [
        [20, 18, 16],
        [18, 17, 15],
        [15, 14, 13],
    ],
    200: [
        [15, 14, 12],
        [14, 13, 11],
        [12, 11, 10],
    ],
}

# Create subplots for each population size
fig, axes = plt.subplots(1, 3, figsize=(18, 6), sharey=True)

for i, pop_size in enumerate(population_sizes):
    ax = axes[i]
    for j, mut_rate in enumerate(mutation_rates):
        ax.plot(crossover_rates, convergence_data[pop_size][j], label=f"Mutation Rate: {mut_rate}")
    ax.set_title(f"Population Size: {pop_size}")
    ax.set_xlabel("Crossover Rate")
    if i == 0:
        ax.set_ylabel("Convergence Rate")
    ax.legend()

plt.tight_layout()
plt.show()
