/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2016  Dirk Beyer
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
package org.sosy_lab.cpachecker.cpa.smg.objects.dls;

import com.google.common.collect.Iterables;

import org.sosy_lab.cpachecker.cpa.smg.SMGAbstractionCandidate;
import org.sosy_lab.cpachecker.cpa.smg.SMGEdgeHasValue;
import org.sosy_lab.cpachecker.cpa.smg.SMGEdgeHasValueFilter;
import org.sosy_lab.cpachecker.cpa.smg.SMGEdgePointsTo;
import org.sosy_lab.cpachecker.cpa.smg.SMGInconsistentException;
import org.sosy_lab.cpachecker.cpa.smg.SMGState;
import org.sosy_lab.cpachecker.cpa.smg.SMGTargetSpecifier;
import org.sosy_lab.cpachecker.cpa.smg.SMGUtils;
import org.sosy_lab.cpachecker.cpa.smg.graphs.CLangSMG;
import org.sosy_lab.cpachecker.cpa.smg.join.SMGJoinStatus;
import org.sosy_lab.cpachecker.cpa.smg.join.SMGJoinSubSMGsForAbstraction;
import org.sosy_lab.cpachecker.cpa.smg.objects.SMGObject;

import java.util.HashMap;
import java.util.Map;

public class SMGDoublyLinkedListCandidateSequence implements SMGAbstractionCandidate {

  private final SMGDoublyLinkedListCandidate candidate;
  private final int length;
  private final SMGJoinStatus seqStatus;

  public SMGDoublyLinkedListCandidateSequence(SMGDoublyLinkedListCandidate pCandidate,
      int pLength, SMGJoinStatus pSmgJoinStatus) {
    candidate = pCandidate;
    length = pLength;
    seqStatus = pSmgJoinStatus;
  }

  public SMGDoublyLinkedListCandidate getCandidate() {
    return candidate;
  }

  public int getLength() {
    return length;
  }

  @Override
  public CLangSMG execute(CLangSMG pSMG, SMGState pSmgState) throws SMGInconsistentException {

    SMGObject prevObject = candidate.getObject();
    int nfo = candidate.getNfo();
    int pfo = candidate.getPfo();

    pSmgState.pruneUnreachable();

    // Abstraction not reachable
    if(!pSMG.getHeapObjects().contains(prevObject)) {
      return pSMG;
    }

    for (int i = 1; i < length; i++) {

      SMGEdgeHasValue nextEdge = Iterables.getOnlyElement(pSMG.getHVEdges(SMGEdgeHasValueFilter.objectFilter(prevObject).filterAtOffset(nfo)));
      SMGObject nextObject = pSMG.getPointer(nextEdge.getValue()).getObject();

      if (length > 1) {
        SMGJoinSubSMGsForAbstraction jointest =
            new SMGJoinSubSMGsForAbstraction(new CLangSMG(pSMG), prevObject, nextObject, candidate,
                pSmgState);

        if (!jointest.isDefined()) {
          return pSMG;
        }
      }

      SMGJoinSubSMGsForAbstraction join =
          new SMGJoinSubSMGsForAbstraction(pSMG, prevObject, nextObject, candidate, pSmgState);

      if(!join.isDefined()) {
        throw new AssertionError("Unexpected join failure while abstracting longest mergeable sequence");
      }

      SMGObject newAbsObj = join.getNewAbstractObject();

      Map<Integer, Integer> reached = new HashMap<>();

      for (SMGEdgePointsTo pte : SMGUtils.getPointerToThisObject(nextObject, pSMG)) {
        pSMG.removePointsToEdge(pte.getValue());

        if (pte.getTargetSpecifier() == SMGTargetSpecifier.ALL) {
          SMGEdgePointsTo newPte = new SMGEdgePointsTo(pte.getValue(), newAbsObj, pte.getOffset(),
              SMGTargetSpecifier.ALL);
          pSMG.addPointsToEdge(newPte);
        } else {

          if (reached.containsKey(pte.getOffset())) {
            int val = reached.get(pte.getOffset());
            pSMG.mergeValues(val, pte.getValue());
          } else {
            SMGEdgePointsTo newPte = new SMGEdgePointsTo(pte.getValue(), newAbsObj, pte.getOffset(),
                SMGTargetSpecifier.LAST);
            pSMG.addPointsToEdge(newPte);
            reached.put(newPte.getOffset(), newPte.getValue());
          }
        }
      }

      reached.clear();

      for (SMGEdgePointsTo pte : SMGUtils.getPointerToThisObject(prevObject, pSMG)) {
        pSMG.removePointsToEdge(pte.getValue());

        if (pte.getTargetSpecifier() == SMGTargetSpecifier.ALL) {
          SMGEdgePointsTo newPte = new SMGEdgePointsTo(pte.getValue(), newAbsObj, pte.getOffset(),
              SMGTargetSpecifier.ALL);
          pSMG.addPointsToEdge(newPte);
        } else {

          if (reached.containsKey(pte.getOffset())) {
            int val = reached.get(pte.getOffset());
            pSMG.mergeValues(val, pte.getValue());
          } else {
            SMGEdgePointsTo newPte = new SMGEdgePointsTo(pte.getValue(), newAbsObj, pte.getOffset(),
                SMGTargetSpecifier.FIRST);
            pSMG.addPointsToEdge(newPte);
            reached.put(newPte.getOffset(), newPte.getValue());
          }
        }
      }

      SMGEdgeHasValue prevObj1hve = Iterables.getOnlyElement(pSMG.getHVEdges(SMGEdgeHasValueFilter.objectFilter(prevObject).filterAtOffset(pfo)));
      SMGEdgeHasValue nextObj2hve = Iterables.getOnlyElement(pSMG.getHVEdges(SMGEdgeHasValueFilter.objectFilter(nextObject).filterAtOffset(nfo)));

      for (SMGObject obj : join.getNonSharedObjectsFromSMG1()) {
        pSMG.removeHeapObjectAndEdges(obj);
      }

      for (SMGObject obj : join.getNonSharedObjectsFromSMG2()) {
        pSMG.removeHeapObjectAndEdges(obj);
      }

      pSMG.removeHeapObjectAndEdges(nextObject);
      pSMG.removeHeapObjectAndEdges(prevObject);
      prevObject = newAbsObj;

      SMGEdgeHasValue nfoHve = new SMGEdgeHasValue(nextObj2hve.getType(), nextObj2hve.getOffset(), newAbsObj, nextObj2hve.getValue());
      SMGEdgeHasValue pfoHve = new SMGEdgeHasValue(prevObj1hve.getType(), prevObj1hve.getOffset(), newAbsObj, prevObj1hve.getValue());
      pSMG.addHasValueEdge(nfoHve);
      pSMG.addHasValueEdge(pfoHve);

      pSmgState.pruneUnreachable();
    }

    return pSMG;
  }

  @Override
  public String toString() {
    return "SMGDoublyLinkedListCandidateSequence [candidate=" + candidate + ", length=" + length
        + "]";
  }

  @Override
  public int getScore() {
    return getLength() + getStatusScore();
  }

  private int getStatusScore() {
    switch (seqStatus) {
      case EQUAL:
        return 3;
      case LEFT_ENTAIL:
      case RIGHT_ENTAIL:
        return 2;
      case INCOMPARABLE:
      default:
        return 0;
    }
  }
}