package at.jwe.planetracer.data.record.data;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataPoint {
    private Long x;
    private Long y;
    private Long id;
}
