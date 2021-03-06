package main;

import java.util.Random;

public class Individual {
    char[] genes;
    float fitness;
    int age;
    private int len;

    Individual(int length) {
        genes = new char[length];
        int ind_rand;

        for (int j = 0; j < length; j++) {
            //We pick a random index for an alphabet letter
            ind_rand = new Random().nextInt(Main.str_alphabet.length);
            //We assign it a random allele
            genes[j] = Main.str_alphabet[ind_rand];
        }
        this.fitness = -1f;
        this.age = 0;
    }

    private Individual(char[] genes) {
        this.genes = new char[genes.length];
        System.arraycopy(genes, 0, this.genes, 0, genes.length);
        this.fitness = -1f;
        this.age = 0;
    }

    //calculate the fitness of a chromosome compared to the password.
    //Correct character choice and placement increment fitness by one, else if it's misplaced it increment by 0.1
    void calcFitness(String password) {
        //if the fitness has already been calculated, return
        if (fitness != -1f) return;

        fitness = 0f;
        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) == genes[i])
                fitness++;
                //If the character appear in the password
            else if (password.indexOf(genes[i]) >= 0)
                fitness += 0.125;
        }
        len = password.length();
    }

    //We choose a gene at random and randomly modify its value.
    private void randomResetMutation() {
        //We choose a gene at random
        int ind_gene_rand =  new Random().nextInt(genes.length);

        //We modify its value at random
        char newGene = Main.str_alphabet[new Random().nextInt(Main.str_alphabet.length)];
        genes[ind_gene_rand] = newGene;
    }

    //We choose two gene at randoms and swap their values
    private void swapMutation() {
        //We pick two genes at random
        int ind_gene_rand1 = new Random().nextInt(genes.length);
        int ind_gene_rand2 = new Random().nextInt(genes.length);

        //We repick the second one if it's the same gene than the first one.
        while (ind_gene_rand1 == ind_gene_rand2)
            ind_gene_rand2 = new Random().nextInt(genes.length);

        //We swap their alleles ( values )
        char temp = genes[ind_gene_rand1];
        genes[ind_gene_rand1] = genes[ind_gene_rand2];
        genes[ind_gene_rand2] = temp;
    }

    //We shuffle a random part of the chromosome.
    private void scrambleMutation() {
        Random rand = new Random();
        int ind_shuffle_start;

        //We pick a random range for the scrumbling (the number of genes that will be shuffled)
        //From 2 to genes.length/2+2
        int range_shuffle = rand.nextInt((genes.length/2)) + 2;

        //We pick a random point where to start the shuffling, until it can't trigger any out of bound errors.
        do {
            ind_shuffle_start = rand.nextInt(genes.length - 2);
        } while (ind_shuffle_start + range_shuffle > genes.length);

        //We shuffle the characters in the given range.
        for (int i = ind_shuffle_start; i < ind_shuffle_start + range_shuffle; i++) {
            int index = rand.nextInt(range_shuffle) + ind_shuffle_start;
            // Simple swap
            char temp = genes[index];
            genes[index] = genes[i];
            genes[i] = temp;
        }
    }

    //Try to mutate the gene given its mutation chance, return true if it mutated.
    void tryMutate() {
        Random rand = new Random();
        //We generate a number between 0 and 1 and if it's <= to the mutation chance, the chromosome mutate.
        float mutationChance = 0.01f;
        if (rand.nextFloat() <= mutationChance) {
            //We choose a random mutation to apply, with increasing scrumbling chance on a higher decimal fitness part.
            float dFitness = fitness - (int)fitness;
            if (rand.nextFloat() <= dFitness)
                scrambleMutation();
            else {
                if (rand.nextInt(1) == 1)
                    randomResetMutation();
                else
                    swapMutation();
            }
        }
    }

    void aging() {
        age++;
    }

    Individual copy() {
        return new Individual(genes);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (char gene : genes)
            str.append(gene);
        str.append(", Fitness: %").append(100*fitness/len).append(", Age: ").append(age);
        return str.toString();
    }

    int getLength() {
        return genes.length;
    }
}
