/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculo.silo;

import java.io.FileReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Diogo
 */
public class CalculoSilo {

    private static int DIMENSAO_MATRIZ = 8; //8x8
    private static int LARGURA_SILO = 320;
    private static int COMPRIMENTO_SILO = 320;
    private static int ALTURA_SILO = 600;
    private static int AREA_QUADRANTE = 1600;
    
    public static void main(String[] args) {
        DecimalFormat decimal = new DecimalFormat("0.##");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        
        JSONParser parser = new JSONParser();
 
        try {
            Object arquivo = parser.parse(new FileReader("src/medicao/medicao-silo.json"));
            double volume = LARGURA_SILO * COMPRIMENTO_SILO * ALTURA_SILO;
            double volumeMetroCub = volume / Math.pow(100, 3); //100³
            
            JSONArray medicoes = (JSONArray) arquivo;
            
            for (int i = 0; i < medicoes.size(); i++) {
                BigDecimal soma = new BigDecimal(0);
                BigDecimal livre;
                BigDecimal ocupado;
                                
                Date data = df.parse(((JSONObject) medicoes.get(i)).get("datetime").toString());
                JSONObject medicao = (JSONObject)((JSONObject) medicoes.get(i)).get("distances");
                
                for (int j = 0; j < DIMENSAO_MATRIZ; j++) {
                    JSONObject colunas = (JSONObject) medicao.get("c" + (j +1));
                    
                    for (int k = 0; k < DIMENSAO_MATRIZ; k++) {
                        soma = soma.add(new BigDecimal(colunas.get("p" + (k +1)).toString()));
                    }
                    
                    livre = soma.multiply(new BigDecimal(AREA_QUADRANTE)).divide(new BigDecimal(100).pow(3)).setScale(2, BigDecimal.ROUND_UP);
                    ocupado = new BigDecimal(volume).divide(new BigDecimal(100).pow(3)).subtract(livre).setScale(2, BigDecimal.ROUND_UP);
                    
                    String livrePer = decimal.format((livre.doubleValue() * 100) / volumeMetroCub);
                    String ocupadoPer = decimal.format((ocupado.doubleValue() * 100) / volumeMetroCub);
                    
                    System.out.println("\nLeitura:" + sdf.format(data) + "\nLivre: " + livre + "m³(" + livrePer + "%)\nOcupado: " + ocupado + "m³(" + ocupadoPer + "%)");
                }
                
            }
 
            /*String data = (String) jsonObject.get("datetime");
            String author = (String) jsonObject.get("Author");
            JSONArray companyList = (JSONArray) jsonObject.get("Company List");*/
 
            //System.out.println("Date: " + medicoes.get(0));
           /* System.out.println("Author: " + author);
            System.out.println("\nCompany List:");
            Iterator<String> iterator = companyList.iterator();*/
           /* while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }*/
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
