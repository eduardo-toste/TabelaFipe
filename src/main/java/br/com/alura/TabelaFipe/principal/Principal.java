package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.service.ConsumoApi;

import java.util.Scanner;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private String endereco;

    public void exibeMenu() {
        String menu = """
                *** OPCOES ***
                Carro
                Moto
                Caminhao
                """;

        System.out.println(menu);
        System.out.println("Insira a opcao desejada: ");

        String opcao = scanner.nextLine();

        if (opcao.toLowerCase().contains("carr")) {
            endereco = URL_BASE + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        } else if (opcao.toLowerCase().contains("caminh")) {
            endereco = URL_BASE + "caminhoes/marcas";
        } else {
            System.out.println("Opcao nao encontrada");
        }

        var json = consumoApi.obterDados(endereco);
        System.out.println(json);
    }
}