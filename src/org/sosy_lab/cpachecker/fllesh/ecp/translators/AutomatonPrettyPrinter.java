package org.sosy_lab.cpachecker.fllesh.ecp.translators;

import java.util.HashMap;
import java.util.Map;

import org.sosy_lab.cpachecker.fllesh.ecp.ECPEdgeSet;
import org.sosy_lab.cpachecker.fllesh.ecp.ECPGuard;
import org.sosy_lab.cpachecker.fllesh.ecp.ECPNodeSet;
import org.sosy_lab.cpachecker.fllesh.ecp.ECPPredicate;
import org.sosy_lab.cpachecker.fllesh.util.Automaton;

public class AutomatonPrettyPrinter {
  
  public static String print(Automaton<? extends GuardedLabel> pAutomaton) {
    AutomatonPrettyPrinter lPrettyPrinter = new AutomatonPrettyPrinter();
    
    return lPrettyPrinter.printPretty(pAutomaton);
  }
  
  private Map<Automaton<? extends GuardedLabel>.State, String> mStateIds;
  private Map<ECPEdgeSet, String> mEdgeSetIds;
  private Map<ECPNodeSet, String> mNodeSetIds;
  private Visitor mVisitor;
  
  public AutomatonPrettyPrinter() {
    mStateIds = new HashMap<Automaton<? extends GuardedLabel>.State, String>();
    mEdgeSetIds = new HashMap<ECPEdgeSet, String>();
    mNodeSetIds = new HashMap<ECPNodeSet, String>();
    mVisitor = new Visitor();
  }
  
  private String getId(Automaton<? extends GuardedLabel>.State pState) {
    if (!mStateIds.containsKey(pState)) {
      mStateIds.put(pState, "S" + mStateIds.size());
    }
    
    return mStateIds.get(pState);
  }
  
  private String getId(ECPEdgeSet pEdgeSet) {
    if (!mEdgeSetIds.containsKey(pEdgeSet)) {
      mEdgeSetIds.put(pEdgeSet, "E" + mEdgeSetIds.size());
    }
    
    return mEdgeSetIds.get(pEdgeSet);
  }
  
  private String getId(ECPNodeSet pNodeSet) {
    if (!mNodeSetIds.containsKey(pNodeSet)) {
      mNodeSetIds.put(pNodeSet, "N" + mNodeSetIds.size());
    }
    
    return mNodeSetIds.get(pNodeSet);
  }
  
  public String printPretty(Automaton<? extends GuardedLabel>.Edge pEdge) {
    return printPretty(pEdge.getSource()) + " -[" + pEdge.getLabel().accept(mVisitor) + "]> " + printPretty(pEdge.getTarget());
  }
  
  public String printPretty(Automaton<? extends GuardedLabel>.State pState) {
    return getId(pState);
  }
  
  public String printPretty(Automaton<? extends GuardedLabel> pAutomaton) {
    StringBuffer lBuffer = new StringBuffer();
    
    boolean lIsFirst = true;
    
    lBuffer.append("States: {");
    for (Automaton<? extends GuardedLabel>.State lState : pAutomaton.getStates()) {
      if (lIsFirst) {
        lIsFirst = false;
      }
      else {
        lBuffer.append(", ");
      }
      lBuffer.append(getId(lState));
    }
    lBuffer.append("}\n");
    
    lBuffer.append("Initial State: ");
    lBuffer.append(getId(pAutomaton.getInitialState()));
    lBuffer.append("\n");
    
    lBuffer.append("Final States: {");
    
    lIsFirst = true;
    
    for (Automaton<? extends GuardedLabel>.State lFinalState : pAutomaton.getFinalStates()) {
      if (lIsFirst) {
        lIsFirst = false;
      }
      else {
        lBuffer.append(", ");
      }
      lBuffer.append(getId(lFinalState));
    }
    lBuffer.append("}\n");
    
    
    
    StringBuffer lTmpBuffer = new StringBuffer();
    
    for (Automaton<? extends GuardedLabel>.Edge lEdge : pAutomaton.getEdges()) {
      //lTmpBuffer.append(getId(lEdge.getSource()) + " -[" + lEdge.getLabel().accept(mVisitor) + "]> " + getId(lEdge.getTarget()));
      lTmpBuffer.append(printPretty(lEdge));
      lTmpBuffer.append("\n");
    }
    
    for (Map.Entry<ECPNodeSet, String> lEntry : mNodeSetIds.entrySet()) {
      lBuffer.append(lEntry.getValue() + ": " + lEntry.getKey().toString());
      lBuffer.append("\n");
    }
    
    for (Map.Entry<ECPEdgeSet, String> lEntry : mEdgeSetIds.entrySet()) {
      lBuffer.append(lEntry.getValue() + ": " + lEntry.getKey().toString());
      lBuffer.append("\n");
    }
    
    lBuffer.append(lTmpBuffer);
    
    return lBuffer.toString();
  }
  
  private class Visitor implements GuardedLabelVisitor<String> {
    @Override
    public String visit(GuardedLambdaLabel pLabel) {
      if (pLabel.hasGuards()) {
        
        StringBuffer lGuardBuffer = new StringBuffer();
        
        lGuardBuffer.append("[");
        
        boolean lIsFirst = true;
        
        for (ECPGuard lGuard : pLabel.getGuards()) {
          if (lIsFirst) {
            lIsFirst = false;
          }
          else {
            lGuardBuffer.append(", ");
          }
          
          if (lGuard instanceof ECPPredicate) {
            lGuardBuffer.append(lGuard.toString());
          }
          else {
            assert(lGuard instanceof ECPNodeSet);
            
            ECPNodeSet lNodeSet = (ECPNodeSet)lGuard;
            
            lGuardBuffer.append(getId(lNodeSet));
          }
        }
        
        lGuardBuffer.append("]");
        
        return "Lambda " + lGuardBuffer.toString();
      }
      else {
        return "Lambda";
      }
    }

    @Override
    public String visit(GuardedEdgeLabel pLabel) {
      String lPrefix = "";
      
      if (pLabel instanceof InverseGuardedEdgeLabel) {
        lPrefix = "!";
      }
      
      if (pLabel.hasGuards()) {
        StringBuffer lGuardBuffer = new StringBuffer();
        
        lGuardBuffer.append("[");
        
        boolean lIsFirst = true;
        
        for (ECPGuard lGuard : pLabel.getGuards()) {
          if (lIsFirst) {
            lIsFirst = false;
          }
          else {
            lGuardBuffer.append(", ");
          }
          
          if (lGuard instanceof ECPPredicate) {
            lGuardBuffer.append(lGuard.toString());
          }
          else {
            assert(lGuard instanceof ECPNodeSet);
            
            ECPNodeSet lNodeSet = (ECPNodeSet)lGuard;
            
            lGuardBuffer.append(getId(lNodeSet));
          }
        }
        
        lGuardBuffer.append("]");
        
        return lPrefix + getId(pLabel.getEdgeSet()) + " " + lGuardBuffer.toString();
      }
      else {
        return lPrefix + getId(pLabel.getEdgeSet());
      }
    }
  }
}
