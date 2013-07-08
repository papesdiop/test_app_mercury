

package com.mercury.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pape S. Diop <papesdiop@gmail.com> papesdiop
 */
/*
 Cette entite permet l'enregistrement des mots envoyes qui devront etre enregistres dans la base de donnees
 */
@Entity
@Table(name = "words")
@XmlRootElement
public class Word implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "libelle")
    private String libelle;
    @Basic(optional = false)
   // @NotNull
    @Column(name = "version")
    @Temporal(TemporalType.TIMESTAMP)
    private Date version;

    public Word() {
    }
    
    @PrePersist
    public void checkVersionIfNotNull(){
        if(this.version==null) {this.version = Calendar.getInstance().getTime();}
    }

    public Word(Integer id) {
        this.id = id;
    }

    public Word(Integer id, String libelle, Date version) {
        this.id = id;
        this.libelle = libelle;
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Date getVersion() {
        return version;
    }

    public void setVersion(Date version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Word)) {
            return false;
        }
        Word other = (Word) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mercury.entity.Words[ id=" + id + " ]";
    }

}
