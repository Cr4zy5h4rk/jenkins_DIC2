package com.qr_auth.demo.controller;

import com.qr_auth.demo.Classes.Utilisateur;
import com.qr_auth.demo.repository.UtilisateurRepository;
import com.qr_auth.demo.services.dto.TransfertTicketRequest;
import com.qr_auth.demo.services.restauService;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:4200/")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/qr_auth/users/tickets")
public class restauController {

    private final UtilisateurRepository userRepo;

    private final restauService restauService;

    public restauController(UtilisateurRepository userRepo,
                            restauService restauService) {
        this.userRepo = userRepo;
        this.restauService = restauService;
    }

    @PostMapping("transfert_ticket")
    private void TransfertTicket(
            @RequestBody TransfertTicketRequest transfertTicketRequest
    )
    {

        if (userRepo.findById(transfertTicketRequest.getMailSender()).isPresent() && userRepo.findById(transfertTicketRequest.getMailReceiver()).isPresent()) {
            System.out.println("mail_sender: " + transfertTicketRequest.getMailSender() + " mail_receiver: " + transfertTicketRequest.getMailReceiver());
            Utilisateur sender = userRepo.findById(transfertTicketRequest.getMailSender()).get();
            Utilisateur receiver = userRepo.findById(transfertTicketRequest.getMailReceiver()).get();

            restauService.sendTickets(receiver, sender, transfertTicketRequest.getQttTicketDej(), transfertTicketRequest.getQttTicketPetitDej());
        }else{
            throw new IllegalArgumentException("Receiver doesn't exist");
        }

    }
}
