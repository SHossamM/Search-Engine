/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Sahar
 */
public class Crawler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        CrawlController c = new CrawlController(10);
        c.Start();
    }
    
}
