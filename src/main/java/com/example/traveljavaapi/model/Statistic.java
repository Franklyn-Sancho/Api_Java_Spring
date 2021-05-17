import java.math.BigDecimal;

import jdk.jfr.DataAmount;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Statistic {

    private BigDecimal sum;
    private BigDecimal avg;
    private BigDecimal max;
    private BigDecimal min;
    private long count;
}