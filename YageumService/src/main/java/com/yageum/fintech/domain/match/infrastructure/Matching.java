package com.yageum.fintech.domain.match.infrastructure;

import com.yageum.fintech.domain.house.infrastructure.House;
import com.yageum.fintech.domain.match.dto.request.UpdateMatchStateDto;
import com.yageum.fintech.domain.tenant.infrastructure.Tenant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "matching",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"tenant_id", "house_id"})} )
public class Matching{

    @Id
    @Column(name = "match_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchState state;

    public void update(UpdateMatchStateDto updateMatchStateDto) {
        this.state = updateMatchStateDto.getState();
     }
}
