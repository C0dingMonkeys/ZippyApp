package com.example.zippy0001.classes;

import java.util.InputMismatchException;

    /** @noinspection ALL*/
    public class ValidaCPF {

        public static boolean isCPF(String CPF) {

            // considera-se erro CPF"s formados por uma sequencia de numeros iguais
            if (CPF.equals("000.000.000-00") ||
                    CPF.equals("111.111.111-11") ||
                    CPF.equals("222.222.222-22") || CPF.equals("333.333.333-33") ||
                    CPF.equals("444.444.444-44") || CPF.equals("555.555.555-55") ||
                    CPF.equals("666.666.666-66") || CPF.equals("777.777.777-77") ||
                    CPF.equals("888.8888.88-88") || CPF.equals("999.999.999-99") ||
                    (CPF.length() != 14))
                return(false);

            CPF = CPF.replace(".","").replace("-","").trim();



            char dig10;
            char dig11;

            int sm, i, r, num, peso;

            // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
            try {
                // Calculo do 1o. Digito Verificador
                sm = 0;
                peso = 10;
                for (i=0; i<9; i++) {
                    // converte o i-esimo caractere do CPF em um numero:
                    // por exemplo, transforma o caractere "0" no inteiro 0
                    // (48 eh a posicao de "0" na tabela ASCII)
                    num = (int)(CPF.charAt(i) - 48);
                    sm = sm + (num * peso);
                    peso = peso - 1;
                }

                r = 11 - (sm % 11);
                if ((r == 10) || (r == 11))
                    dig10 = '0';
                else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

                // Calculo do 2o. Digito Verificador
                sm = 0;
                peso = 11;
                for(i=0; i<10; i++) {
                    num = (int)(CPF.charAt(i) - 48);
                    sm = sm + (num * peso);
                    peso = peso - 1;
                }

                r = 11 - (sm % 11);
                if ((r == 10) || (r == 11))
                    dig11 = '0';
                else dig11 = (char)(r + 48);

                // Verifica se os digitos calculados conferem com os digitos informados.
                return (dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10));
            } catch (InputMismatchException erro) {
                return(false);
            }
        }
    }
