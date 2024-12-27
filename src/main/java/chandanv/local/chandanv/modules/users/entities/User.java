package chandanv.local.chandanv.modules.users.entities;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.FetchType;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name="user_catalogue_id")
    private Long userCatalogueId;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
        name = "user_catalogue_user",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns= @JoinColumn(name = "user_catalogue_id")
    )
    private Set<UserCatalogue> userCatalogues = new HashSet<>();

    private String name;
    private String email;
    private String password;
    private String phone;
    private String image;
    private String address;

    @Column(name="created_at", updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreated(){
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdated(){
        updatedAt = LocalDateTime.now();
    }

    public Long getUserCatalogueId(){
        return userCatalogueId;
    }

    public void setUserCatalogueId(Long userCatalogueId){
        this.userCatalogueId = userCatalogueId;
    }

  

}
