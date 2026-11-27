package cv.gov.dge.paef.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_pedido_pendente", schema = "paef")
@Getter
@Setter
@NoArgsConstructor
public class TPedidoPendenteEntity {
    @Id
    private Integer id;
    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;
    @Column(name = "dm_estado")
    private String dmEstado;
    @Column(name = "process_type")
    private String processType;
    @Column(name = "process_type_name")
    private String processTypeName;
    @Column(name = "task_cod")
    private String taskCod;
    @Column(name = "task_name")
    private String taskName;
    @Column(name = "user_registo")
    private String userRegisto;
    @Column(name = "id_alvara")
    private String idAlvara;
    @Column(name = "id_entidade")
    private String idEntidade;
    @Column(name = "possui_form_out_catalog")
    private Boolean possuiFormOutCatalog;
    @Column(name = "fase_atual")
    private String faseAtual;
    @Column(name = "id_pedido")
    private String idPedido;
    private String obs;
    @Column(name = "possui_form_distancia")
    private Boolean possuiFormDistancia;
}
