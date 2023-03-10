/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.lang.model.util.ElementScanner6;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator extends Thread{

    private static final int BLACK_LIST_ALARM_COUNT=5;
    private Semaforo miBandera;
    
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int N){
        int datosProcesar;
        miBandera = new Semaforo();
        int ocurrencesCount=0;
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        
        if(N%2==0){
            datosProcesar= blackListOcurrences.size()/N;
        }else{


        }  
        

        
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        
        int checkedListsCount=0;
        
        for (int i=0;i<skds.getRegisteredServersCount() && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
            checkedListsCount++;
            
            if (skds.isInBlackListServer(i, ipaddress)){
                
                blackListOcurrences.add(i);
                
                ocurrencesCount++;
            }
        }
        
        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            PausarHilo();
            skds.reportAsNotTrustworthy(ipaddress);

        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }                
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        
        return blackListOcurrences;
    }

    public
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());

    public void despausarHilos(){
        miBandera.setBandera(true);
        synchronized(miBandera).notifyAll();
    }

    public void PausarHilo(){
        miBandera.setBandera(false);
        synchronized(miBandera).wait();
    }
    

    
}
