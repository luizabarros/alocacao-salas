package com.example.alocacao.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.alocacao.entities.DiaDaSemana;

public class AulaDTO {
    private UUID id;
    private UUID disciplinaId;
    private UUID salaId;
    private DiaDaSemana diaDaSemana;
    private LocalDateTime horaInicio;
    private int duracao;

    public AulaDTO() {}


    public AulaDTO(UUID id, UUID disciplinaId, UUID salaId, DiaDaSemana diaDaSemana, LocalDateTime horaInicio, int duracao) {
        this.id = id;
        this.disciplinaId = disciplinaId;
        this.salaId = salaId;
        this.diaDaSemana = diaDaSemana;
        this.horaInicio = horaInicio;
        this.duracao = duracao;
    }


	public UUID getId() {
		return id;
	}


	public void setId(UUID id) {
		this.id = id;
	}


	public UUID getDisciplinaId() {
		return disciplinaId;
	}


	public void setDisciplinaId(UUID disciplinaId) {
		this.disciplinaId = disciplinaId;
	}


	public UUID getSalaId() {
		return salaId;
	}


	public void setSalaId(UUID salaId) {
		this.salaId = salaId;
	}


	public DiaDaSemana getDiaDaSemana() {
		return diaDaSemana;
	}


	public void setDiaDaSemana(DiaDaSemana diaDaSemana) {
		this.diaDaSemana = diaDaSemana;
	}


	public LocalDateTime getHoraInicio() {
		return horaInicio;
	}


	public void setHoraInicio(LocalDateTime horaInicio) {
		this.horaInicio = horaInicio;
	}


	public int getDuracao() {
		return duracao;
	}


	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}
    
    
}
