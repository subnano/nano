package net.subnano.codec.wire;

import net.subnano.codec.sample.Price;
import org.assertj.core.api.AbstractAssert;

public class PriceAssert extends AbstractAssert<PriceAssert, Price> {

    private PriceAssert(Price actual) {
        super(actual, PriceAssert.class);
    }

    public static PriceAssert assertThat(Price actual) {
        return new PriceAssert(actual);
    }

    @Override
    public PriceAssert isEqualTo(Object expected) {
        return super.isEqualTo(expected);
    }
}
