package remy.pouzet.moodtracker.model;

/**
 * Created by Remy Pouzet on 02/07/2019.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
/**


 * Classe permettant de travailler avec une chronologie basee sur le format jour/heure/minute/seconde

 *


 */
public class JourHeure extends Date {
    private static final long serialVersionUID = 4562804277680890199L;
    SimpleDateFormat formatHeure = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat formatJour = new SimpleDateFormat("D");
    /**


     * constructeur de base initialise a 00:00:00

     */
    private JourHeure(){
        this(0,"00:00:00");
    }
    /**


     * constructeur par saisi du nombre de jour et de l'heure

     * @param p_NbrJour au format int

     * @param p_heure au format HH:MM:SS

     */
    public JourHeure(int p_NbrJour, String p_heure){
        super();
        Date d = new Date(0);
        formatHeure.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        formatJour.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        try {
            d = formatHeure.parse(p_heure);
            this.setTime(d.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.setTime(86400000L*p_NbrJour+this.getTime());
    }
    /**


     * constructeur avec uniquement l'heure

     * @param p_heure au format HH:MM:SS

     */
    public JourHeure(String p_heure){
        this(0,p_heure);
    }
    //METHODES
    /**


     * methode retournant la difference avec un autre JourHeure

     */
    public JourHeure differenceAvecJourHeure(JourHeure p_jh){
        JourHeure ret_jh=new JourHeure();
        ret_jh.setTime(Math.abs(getTime()-p_jh.getTime()));
        return ret_jh;
    }
    /**


     * methode permettant d'ajouter un temps

     * @param p_jh

     */
    public void calcul_Ajouter(JourHeure p_jh){
        this.setTime(this.getTime()+p_jh.getTime());
    }
    /**


     * methode permettant de soustraire un temps

     * @param p_jh

     */
    public void calcul_Retirer(JourHeure p_jh){
        this.setTime(this.getTime()-p_jh.getTime());
    }
    /**


     * methode toString au format suivant : "3j 15:50:23"

     */
    public String toString(){
        int jour = Integer.parseInt(formatJour.format(this))-1;
        return jour+"j "+formatHeure.format(this);
    }
}
