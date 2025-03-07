package com.qr_auth.demo.services;

import com.qr_auth.demo.Classes.Utilisateur;
import com.qr_auth.demo.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class restauService {

    private final UtilisateurRepository utilisateurRepository;

    public restauService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }


    public void sendTickets(Utilisateur receiver, Utilisateur sender,Integer QttTicketDej, Integer QttTicketPetitDej) {


            if (sender.getNbrTckDej() >= QttTicketDej && sender.getNbrTckPetitDej() >= QttTicketPetitDej) {
                System.out.println("sending dej");
                sender.setNbrTckDej(sender.getNbrTckDej() - QttTicketDej);
                sender.setNbrTckDej(sender.getNbrTckPetitDej() - QttTicketPetitDej);
                receiver.setNbrTckDej(receiver.getNbrTckDej() + QttTicketDej);
                receiver.setNbrTckDej(receiver.getNbrTckPetitDej() + QttTicketPetitDej);
                System.out.println("sended dej");
            }
            else {
                throw new IllegalStateException("Quantite insuffisante pour faire le transfert !!!");
            }


        utilisateurRepository.save(sender);
        utilisateurRepository.save(receiver);
    }
}