package com.documenti.bakend.model;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.management.relation.Role;

import com.documenti.bakend.enums.Rol;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "usuarios")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;          // antes "correo"

    @Column(nullable = false)
    private String password;       // antes "contraseña"

    @Column(nullable = false)
    private String nombre;

    @ElementCollection()
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Set<Rol> roles = new HashSet<>();  // SUPER_ADMIN, EDITOR, LECTOR, etc.

    private boolean activo = false;  // hasta que no confirme invitación

    private String tokenInvitacion;
    
    private LocalDateTime tokenInvitacionExpira;

    @CreationTimestamp
    private LocalDateTime creadoEn;


    // Datos del perfil que varían por dominio (legal, médico, contable)
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata;

    // Farbicas
    public static Usuario crearAdmin(String email, String password, String nombre, Set<Rol> roles) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Por favor ponga un correo");
        }
        if (password == null || password.isBlank()){
            throw new IllegalArgumentException("Por favor poner una contraseña");
        }
        if (nombre == null || nombre.isBlank()){
            throw new IllegalArgumentException("Por favor poner una contraseña");
        }
        Usuario usuario = new Usuario();
        usuario.email = email;
        usuario.password = password;
        usuario.nombre = nombre;
        usuario.roles = Set.of(Rol.SUPER_ADMIN);
        return usuario;
    }


    


    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setTokenInvitacion(String tokenInvitacion) {
        this.tokenInvitacion = tokenInvitacion;
    }

    public void setTokenInvitacionExpira(LocalDateTime tokenInvitacionExpira) {
        this.tokenInvitacionExpira = tokenInvitacionExpira;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    

}
