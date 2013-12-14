/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2013  Dirk Beyer
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
package org.sosy_lab.cpachecker.cpa.arg;


public class ConditionContainer implements IConditionContainer{

  /**
   * Contains state information for while going through an ARGStatePath
   *
   * @param currentState contains the current State
   * @param visitedAssumeEdgesCount contains the current visited assume edges along the path
   * @param visitedStatesCount contains the current visited state
   */
  public ConditionContainer(ARGState currentState, int visitedAssumeEdgesCount, int visitedStatesCount){
    this.currentState = currentState;
    this.visitedAssumeEdgesCount = visitedAssumeEdgesCount;
    this.visitedStatesCount = visitedStatesCount;
  }

  private ARGState currentState;
  private int visitedAssumeEdgesCount;
  private int visitedStatesCount;

  @Override
  public ARGState getCurrentState() {
    return currentState;
  }

  @Override
  public int getVisitedAssumeEdgesCount() {
    return visitedAssumeEdgesCount;
  }

  @Override
  public int getVisitedStatesCount() {
    return visitedStatesCount;
  }

}
