package chandanv.local.chandanv.modules.users.entities;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="user_catalogues")
public class UserCatalogue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


    @Column(name="publish", nullable=false, columnDefinition="TINYINT")
    private Integer publish;

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

    
}
