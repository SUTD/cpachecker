/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2011  Dirk Beyer
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
package org.sosy_lab.cpachecker.cpa.assumptions.progressobserver.heuristics;

import org.sosy_lab.common.LogManager;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.cpachecker.cfa.objectmodel.CFAEdge;
import org.sosy_lab.cpachecker.cfa.objectmodel.CFANode;
import org.sosy_lab.cpachecker.cpa.assumptions.progressobserver.ReachedHeuristicsDataSetView;
import org.sosy_lab.cpachecker.cpa.assumptions.progressobserver.StopHeuristics;
import org.sosy_lab.cpachecker.cpa.assumptions.progressobserver.StopHeuristicsData;

public class TimeOutHeuristics implements StopHeuristics<TimeOutHeuristicsData> {

  private TimeOutHeuristicsPrecision precision;
  private long startTime;
  private long initialStartTime;

  public TimeOutHeuristics(Configuration config, LogManager pLogger)
      throws InvalidConfigurationException {
    precision = new TimeOutHeuristicsPrecision(this, config, pLogger);
  }

  @Override
  public TimeOutHeuristicsData collectData(StopHeuristicsData pData, ReachedHeuristicsDataSetView pReached) {
    return (TimeOutHeuristicsData)pData;
  }

  @Override
  public TimeOutHeuristicsData getInitialData(CFANode pNode) {
    resetStartTime();
    setInitialStartTime();
    return new TimeOutHeuristicsData(false);
  }

  private void setInitialStartTime() {
    initialStartTime = startTime;
  }

  @Override
  public TimeOutHeuristicsData processEdge(StopHeuristicsData pData, CFAEdge pEdge) {
    TimeOutHeuristicsData d = (TimeOutHeuristicsData)pData;
    if (d == TimeOutHeuristicsData.BOTTOM)
      return d;
    else
      if(precision.getHardLimitThreshold()!=-1 &&
          System.currentTimeMillis() > initialStartTime + precision.getHardLimitThreshold()){
        precision.setShouldForceToStop();
        d.setThreshold(precision.getHardLimitThreshold());
        return TimeOutHeuristicsData.BOTTOM;
      }
      if (System.currentTimeMillis() > startTime + precision.getThreshold()) {
        d.setThreshold(precision.getThreshold());
        return TimeOutHeuristicsData.BOTTOM;
      }
      else
        return d;
  }

  public void resetStartTime() {
    startTime = System.currentTimeMillis();
  }

  @Override
  public HeuristicPrecision getPrecision() {
    return precision;
  }

}
