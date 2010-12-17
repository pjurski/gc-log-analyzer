package org.pjurski.gclog.analyzer;

import org.pjurski.gclog.parser.GcLogCCConstants;

public interface IAnalyzerConstants {

	public int UNKNOWN_GC = GcLogCCConstants.GC_FULL_GC;

	public int FULL_GC = GcLogCCConstants.GC_FULL_GC;

	public int GC = GcLogCCConstants.GC_GC;
}