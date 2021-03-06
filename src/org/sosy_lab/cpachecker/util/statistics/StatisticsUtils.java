/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2014  Dirk Beyer
 *  All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 *  CPAchecker web page:
 *    http://cpachecker.sosy-lab.org
 */
package org.sosy_lab.cpachecker.util.statistics;

import java.io.PrintStream;
import java.util.Locale;

import com.google.common.base.Strings;


public class StatisticsUtils {
  private StatisticsUtils() { }

  public static String toPercent(double val, double full) {
    return String.format("%1.0f", val/full*100) + "%";
  }

  public static String valueWithPercentage(int value, int totalCount) {
    return value + " (" + toPercent(value, totalCount) + ")";
  }

  public static String div(double val, double full) {
    return String.format(Locale.ROOT, "%.2f", val/full);
  }

  public static void write(PrintStream target, int indentLevel, int outputNameColWidth,
      String name, Object value) {
    String indentation = Strings.repeat("  ", indentLevel);
    target.println(String.format("%-" + outputNameColWidth + "s %s",
                                 indentation + name + ":", value));
  }

  public static void write(PrintStream target, int indentLevel, int outputNameColWidth,
      AbstractStatValue stat) {
    write(target, indentLevel, outputNameColWidth, stat.getTitle(), stat.toString());
  }
}
