package at.jwe.snyder.data.record.data;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataPoint {
    private int x;
    private int y;
    private int id;
}
