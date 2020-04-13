package main;

public class Main {
    private static String alphabet =
            "abcçdefgğhıijklmnoöpqrsştuüvwxyzABCÇDEFGĞHIİJKLMNOÖPQRSŞTUÜVWXYZ_-*/+!'?=&%$#,.:<>|()[]{}0123456789";
    static char[] str_alphabet = alphabet.toCharArray();

    public static void main(String[] args) {
        String password = "İşlemselZeka_DL_20";
        int nb_individuals = 500;
        int max_gen = 100;

        long startTime;
        long endTime;

        System.out.println("\nWord to find: \'" + password + "\'");
        System.out.println("Population size: " + nb_individuals);
        System.out.println("Maximum number of generations: " + max_gen);
        for (int i = 1; i < 11; i++) {
            System.out.println("\nRun #" + i);

            startTime = System.nanoTime();
            run(password, nb_individuals, max_gen);
            endTime = System.nanoTime();

            System.out.println("Duration: " + (endTime - startTime) + " ms");
        }
    }

    private static void run(String password, int nb_individuals, int max_gen){
        /* We generate a random population :
           We generate N genes of the same length than the password with random alleles, or we can say
           We generate N char[] with the same length than the password with random str_alphabet characters in each slot.
        */
        Population population = new Population(password.length(), nb_individuals);
        population.calculateFitness(password);
        Individual bestFit = population.bestFitness();

        //This is generation 1, we keep making new generations till we find an individual with the perfect fitness.
        int nb_gen = 1;
        while (population.bestFitnessInPopulation() != password.length() && nb_gen < max_gen) {
            population.newGeneration();
            population.calculateFitness(password);
            nb_gen++;

            bestFit = population.bestFitness();
        }

        System.out.println("Best result: " + bestFit.toString() + "\nAt generation #" + nb_gen);
        System.out.println("Number of mutation: " + population.getMutationCount());
    }
}
