package com.qr_auth.demo.services.mapper;

import com.qr_auth.demo.Classes.Utilisateur;
import com.qr_auth.demo.services.dto.EtudiantDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface EtudiantMapper extends EntityMapper<EtudiantDTO, Utilisateur> {

    EtudiantDTO toDto(Utilisateur etudiant);

    Utilisateur toEntity(EtudiantDTO etudiantDTO);
}
