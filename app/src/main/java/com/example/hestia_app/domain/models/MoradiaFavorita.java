package com.example.hestia_app.domain.models;

import java.util.List;
import java.util.UUID;

public class MoradiaFavorita {
    private UUID idUsuario;
    private List<UUID> moradias_ids;

    public MoradiaFavorita(UUID idUsuario, List<UUID> moradias_ids) {
        this.idUsuario = idUsuario;
        this.moradias_ids = moradias_ids;
    }

    public UUID getIdUsuario() {
        return idUsuario;
    }

    public List<UUID> getMoradias_ids() {
        return moradias_ids;
    }
    public void setIdUsuario(UUID idUsuario) {
        this.idUsuario = idUsuario;
    }
    public void setMoradias_ids(List<UUID> moradias_ids) {
        this.moradias_ids = moradias_ids;
    }

    @Override
    public String toString() {
        return "MoradiaFavorita{" +
                "idUsuario=" + idUsuario +
                ", moradias_ids=" + moradias_ids +
                '}';
    }
}
