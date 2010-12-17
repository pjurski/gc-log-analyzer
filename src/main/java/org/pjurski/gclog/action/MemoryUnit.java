package org.pjurski.gclog.action;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum MemoryUnit {
	B {
		@Override
		public BigDecimal convert(BigDecimal v) {
			return v.multiply(VALUE_1024);
		}
	},
	b {
		@Override
		public BigDecimal convert(BigDecimal v) {
			return B.convert(v);
		}
	},
	KB {
		@Override
		public BigDecimal convert(BigDecimal v) {
			return v;
		}
	},
	kb {
		@Override
		public BigDecimal convert(BigDecimal v) {
			return KB.convert(v);
		}
	},
	MB {
		@Override
		public BigDecimal convert(BigDecimal v) {
			return v.divide(VALUE_1024, 50, RoundingMode.HALF_UP);
		}
	},
	mb {
		@Override
		public BigDecimal convert(BigDecimal v) {
			return MB.convert(v);
		}
	},
	GB {
		@Override
		public BigDecimal convert(BigDecimal v) {
			return v.divide(VALUE_1024_x_1024, 50, RoundingMode.HALF_UP);
		}
	},
	gb {
		@Override
		public BigDecimal convert(BigDecimal v) {
			return GB.convert(v);
		}
	};

	private static final BigDecimal VALUE_1024 = new BigDecimal("1024");
	private static final BigDecimal VALUE_1024_x_1024 = VALUE_1024
			.multiply(VALUE_1024);

	public static MemoryUnit getDefault() {
		return MB;
	}

	public BigDecimal convert(BigDecimal v) {
		throw new AbstractMethodError();
	}
}