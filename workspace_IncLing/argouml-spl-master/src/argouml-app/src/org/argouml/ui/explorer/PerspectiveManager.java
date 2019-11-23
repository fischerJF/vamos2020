// $Id: PerspectiveManager.java 132 2010-09-26 23:32:33Z marcusvnac $
// Copyright (c) 1996-2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.ui.explorer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

//#if defined(LOGGING)
//@#$LPS-LOGGING:GranularityType:Import
import org.apache.log4j.Logger;
//#endif
import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.ui.explorer.rules.GoAssocRoleToMessages;
//#if defined(STATEDIAGRAM)
//@#$LPS-STATEDIAGRAM:GranularityType:Import
import org.argouml.ui.explorer.rules.GoBehavioralFeatureToStateDiagram;
//#endif
import org.argouml.ui.explorer.rules.GoBehavioralFeatureToStateMachine;
import org.argouml.ui.explorer.rules.GoClassToAssociatedClass;
import org.argouml.ui.explorer.rules.GoClassToNavigableClass;
import org.argouml.ui.explorer.rules.GoClassToSummary;
import org.argouml.ui.explorer.rules.GoClassifierToBehavioralFeature;
//#if defined(COLLABORATIONDIAGRAM)
//@#$LPS-COLLABORATIONDIAGRAM:GranularityType:Import
import org.argouml.ui.explorer.rules.GoClassifierToCollaboration;
//#endif
import org.argouml.ui.explorer.rules.GoClassifierToInstance;
//#if defined(SEQUENCEDIAGRAM)
//@#$LPS-SEQUENCEDIAGRAM:GranularityType:Import
import org.argouml.ui.explorer.rules.GoClassifierToSequenceDiagram;
//#endif
//#if defined(STATEDIAGRAM)
//@#$LPS-STATEDIAGRAM:GranularityType:Import
import org.argouml.ui.explorer.rules.GoClassifierToStateMachine;
//#endif
import org.argouml.ui.explorer.rules.GoClassifierToStructuralFeature;
//#if defined(COLLABORATIONDIAGRAM)
//@#$LPS-COLLABORATIONDIAGRAM:GranularityType:Import
import org.argouml.ui.explorer.rules.GoCollaborationToDiagram;
import org.argouml.ui.explorer.rules.GoCollaborationToInteraction;
//#endif
import org.argouml.ui.explorer.rules.GoComponentToResidentModelElement;
//#if defined(STATEDIAGRAM)
//@#$LPS-STATEDIAGRAM:GranularityType:Import
import org.argouml.ui.explorer.rules.GoCompositeStateToSubvertex;
//#endif
//#if defined(COGNITIVE)
//@#$LPS-COGNITIVE:GranularityType:Import
import org.argouml.ui.explorer.rules.GoCriticsToCritic;
//#endif
import org.argouml.ui.explorer.rules.GoDiagramToEdge;
import org.argouml.ui.explorer.rules.GoDiagramToNode;
import org.argouml.ui.explorer.rules.GoElementToMachine;
import org.argouml.ui.explorer.rules.GoEnumerationToLiterals;
import org.argouml.ui.explorer.rules.GoGeneralizableElementToSpecialized;
import org.argouml.ui.explorer.rules.GoInteractionToMessages;
import org.argouml.ui.explorer.rules.GoLinkToStimuli;
import org.argouml.ui.explorer.rules.GoMessageToAction;
import org.argouml.ui.explorer.rules.GoModelElementToBehavior;
import org.argouml.ui.explorer.rules.GoModelElementToComment;
import org.argouml.ui.explorer.rules.GoModelElementToContainedDiagrams;
import org.argouml.ui.explorer.rules.GoModelElementToContainedLostElements;
import org.argouml.ui.explorer.rules.GoModelElementToContents;
import org.argouml.ui.explorer.rules.GoModelToBaseElements;
//#if defined(COLLABORATIONDIAGRAM)
//@#$LPS-COLLABORATIONDIAGRAM:GranularityType:Import
import org.argouml.ui.explorer.rules.GoModelToCollaboration;
//#endif
import org.argouml.ui.explorer.rules.GoModelToDiagrams;
import org.argouml.ui.explorer.rules.GoModelToElements;
import org.argouml.ui.explorer.rules.GoModelToNode;
import org.argouml.ui.explorer.rules.GoNamespaceToClassifierAndPackage;
import org.argouml.ui.explorer.rules.GoNamespaceToDiagram;
import org.argouml.ui.explorer.rules.GoNamespaceToOwnedElements;
import org.argouml.ui.explorer.rules.GoNodeToResidentComponent;
//#if defined(COLLABORATIONDIAGRAM)
//@#$LPS-COLLABORATIONDIAGRAM:GranularityType:Import
import org.argouml.ui.explorer.rules.GoOperationToCollaboration;
import org.argouml.ui.explorer.rules.GoOperationToCollaborationDiagram;
//#endif
//#if defined(SEQUENCEDIAGRAM)
//@#$LPS-SEQUENCEDIAGRAM:GranularityType:Import
import org.argouml.ui.explorer.rules.GoOperationToSequenceDiagram;
//#endif
import org.argouml.ui.explorer.rules.GoPackageToClass;
import org.argouml.ui.explorer.rules.GoPackageToElementImport;
import org.argouml.ui.explorer.rules.GoProfileConfigurationToProfile;
//#if defined(COGNITIVE)
//@#$LPS-COGNITIVE:GranularityType:Import
import org.argouml.ui.explorer.rules.GoProfileToCritics;
//#endif
import org.argouml.ui.explorer.rules.GoProfileToModel;
//#if defined(COLLABORATIONDIAGRAM)
//@#$LPS-COLLABORATIONDIAGRAM:GranularityType:Import
import org.argouml.ui.explorer.rules.GoProjectToCollaboration;
//#endif
import org.argouml.ui.explorer.rules.GoProjectToDiagram;
import org.argouml.ui.explorer.rules.GoProjectToModel;
import org.argouml.ui.explorer.rules.GoProjectToProfileConfiguration;
import org.argouml.ui.explorer.rules.GoProjectToRoots;
//#if defined(STATEDIAGRAM)
//@#$LPS-STATEDIAGRAM:GranularityType:Import
import org.argouml.ui.explorer.rules.GoProjectToStateMachine;
//#endif
import org.argouml.ui.explorer.rules.GoSignalToReception;
//#if defined(STATEDIAGRAM)
//@#$LPS-STATEDIAGRAM:GranularityType:Import
import org.argouml.ui.explorer.rules.GoStateMachineToState;
import org.argouml.ui.explorer.rules.GoStateMachineToTop;
import org.argouml.ui.explorer.rules.GoStateMachineToTransition;
//#endif
import org.argouml.ui.explorer.rules.GoStateToDoActivity;
import org.argouml.ui.explorer.rules.GoStateToDownstream;
import org.argouml.ui.explorer.rules.GoStateToEntry;
import org.argouml.ui.explorer.rules.GoStateToExit;
//#if defined(STATEDIAGRAM)
//@#$LPS-STATEDIAGRAM:GranularityType:Import
import org.argouml.ui.explorer.rules.GoStateToIncomingTrans;
//#endif
import org.argouml.ui.explorer.rules.GoStateToInternalTrans;
//#if defined(STATEDIAGRAM)
//@#$LPS-STATEDIAGRAM:GranularityType:Import
import org.argouml.ui.explorer.rules.GoStateToOutgoingTrans;
import org.argouml.ui.explorer.rules.GoStatemachineToDiagram;
//#endif
import org.argouml.ui.explorer.rules.GoStereotypeToTagDefinition;
import org.argouml.ui.explorer.rules.GoStimulusToAction;
import org.argouml.ui.explorer.rules.GoSubmachineStateToStateMachine;
import org.argouml.ui.explorer.rules.GoSummaryToAssociation;
import org.argouml.ui.explorer.rules.GoSummaryToAttribute;
import org.argouml.ui.explorer.rules.GoSummaryToIncomingDependency;
import org.argouml.ui.explorer.rules.GoSummaryToInheritance;
import org.argouml.ui.explorer.rules.GoSummaryToOperation;
import org.argouml.ui.explorer.rules.GoSummaryToOutgoingDependency;
import org.argouml.ui.explorer.rules.GoTransitionToGuard;
import org.argouml.ui.explorer.rules.GoTransitionToSource;
import org.argouml.ui.explorer.rules.GoTransitionToTarget;
import org.argouml.ui.explorer.rules.GoTransitiontoEffect;
import org.argouml.ui.explorer.rules.GoUseCaseToExtensionPoint;
import org.argouml.ui.explorer.rules.PerspectiveRule;

/**
 * Provides a model and event management for perspectives(views) of the
 * Explorer.<p>
 *
 * This class defines the complete list of perspective rules, and knows the
 * default perspectives and their contents.
 *
 * @author alexb
 * @since 0.15.2
 */
public final class PerspectiveManager {
    //#if defined(LOGGING)
    //@#$LPS-LOGGING:GranularityType:Field
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(PerspectiveManager.class);
    //#endif
    private static PerspectiveManager instance;

    private List<PerspectiveManagerListener> perspectiveListeners;

    private List<ExplorerPerspective> perspectives;

    private List<PerspectiveRule> rules;

    /**
     * @return the instance (singleton)
     */
    public static PerspectiveManager getInstance() {
        if (instance == null) {
            instance = new PerspectiveManager();
        }
        return instance;
    }

    /**
     * Creates a new instance of PerspectiveManager.
     */
    private PerspectiveManager() {

        perspectiveListeners = new ArrayList<PerspectiveManagerListener>();
        perspectives = new ArrayList<ExplorerPerspective>();
        rules = new ArrayList<PerspectiveRule>();
        loadRules();
    }

    /**
     * @param listener
     *            the listener to be added
     */
    public void addListener(PerspectiveManagerListener listener) {
        perspectiveListeners.add(listener);
    }

    /**
     * @param listener
     *            the listener to be removed
     */
    public void removeListener(PerspectiveManagerListener listener) {
        perspectiveListeners.remove(listener);
    }

    /**
     * @param perspective
     *            the perspective to be added
     */
    public void addPerspective(ExplorerPerspective perspective) {
        perspectives.add(perspective);
        for (PerspectiveManagerListener listener : perspectiveListeners) {
            listener.addPerspective(perspective);
        }
    }

    /**
     * @param newPerspectives
     *            the collection of perspectives to be added
     */
    public void addAllPerspectives(
            Collection<ExplorerPerspective> newPerspectives) {
        for (ExplorerPerspective newPerspective : newPerspectives) {
            addPerspective(newPerspective);
        }
    }

    /**
     * @param perspective
     *            the perspective to be removed
     */
    public void removePerspective(ExplorerPerspective perspective) {
        perspectives.remove(perspective);
        for (PerspectiveManagerListener listener : perspectiveListeners) {
            listener.removePerspective(perspective);
        }
    }

    /**
     * Remove all perspectives.
     */
    public void removeAllPerspectives() {

        List<ExplorerPerspective> pers = new ArrayList<ExplorerPerspective>();

        pers.addAll(getPerspectives());
        for (ExplorerPerspective perspective : pers) {
            removePerspective(perspective);
        }
    }

    /**
     * @return the list of all perspectives
     */
    public List<ExplorerPerspective> getPerspectives() {
        return perspectives;
    }

    /**
     * Tries to load user defined perspectives, if it can't it loads the
     * (predefined) default perspectives.
     */
    public void loadUserPerspectives() {

        String userPerspectives =
            Configuration.getString(
                    Argo.KEY_USER_EXPLORER_PERSPECTIVES, "");

        StringTokenizer pst = new StringTokenizer(userPerspectives, ";");

        if (pst.hasMoreTokens()) {

            // load user perspectives
            while (pst.hasMoreTokens()) {
                String perspective = pst.nextToken();
                StringTokenizer perspectiveDetails =
                    new StringTokenizer(perspective, ",");

                // get the perspective name
                String perspectiveName = perspectiveDetails.nextToken();

                ExplorerPerspective userDefinedPerspective =
                    new ExplorerPerspective(perspectiveName);

                // make sure there are some rules...
                if (perspectiveDetails.hasMoreTokens()) {

                    // get the rules
                    while (perspectiveDetails.hasMoreTokens()) {

                        // get the rule name
                        String ruleName = perspectiveDetails.nextToken();

                        // create the rule
                        try {
                            Class ruleClass = Class.forName(ruleName);

                            PerspectiveRule rule =
                                (PerspectiveRule) ruleClass.newInstance();

                            userDefinedPerspective.addRule(rule);
                        } catch (ClassNotFoundException e) {
                            //#if defined(LOGGING)
                            //@#$LPS-LOGGING:GranularityType:Statement
                            //@#$LPS-LOGGING:Localization:NestedStatement
                        	if(org.argouml.specifications.Configuration.LOGGING) {
                            LOG.error(
                                    "could not create rule " + ruleName 
                                    + " you can try to "
                                    + "refresh the perspectives to the "
                                    + "default settings.",
                                    e);
                        	}
                            //#endif
                        } catch (InstantiationException e) {
                            //#if defined(LOGGING)
                            //@#$LPS-LOGGING:GranularityType:Statement
                            //@#$LPS-LOGGING:Localization:NestedStatement
                        	if(org.argouml.specifications.Configuration.LOGGING) {
                        	LOG.error(
                                    "could not create rule " + ruleName 
                                    + " you can try to "
                                    + "refresh the perspectives to the "
                                    + "default settings.",
                                    e);
                        	}
                            //#endif
                        } catch (IllegalAccessException e) {
                            //#if defined(LOGGING)
                            //@#$LPS-LOGGING:GranularityType:Statement
                            //@#$LPS-LOGGING:Localization:NestedStatement
                        	if(org.argouml.specifications.Configuration.LOGGING) {
                            LOG.error(
                                    "could not create rule " + ruleName 
                                    + " you can try to "
                                    + "refresh the perspectives to the "
                                    + "default settings.",
                                    e);
                        	}//#endif
                        }
                    }
                } else {
                    // rule name but no rules
                    continue;
                }

                // add the perspective
                addPerspective(userDefinedPerspective);
            }
        } else {
            // no user defined perspectives
            loadDefaultPerspectives();
        }

        // one last check that some loaded.
        if (getPerspectives().size() == 0) {
            loadDefaultPerspectives();
        }
    }

    /**
     * Loads a pre-defined default set of perspectives.
     */
    public void loadDefaultPerspectives() {
        Collection<ExplorerPerspective> c = getDefaultPerspectives();

        addAllPerspectives(c);
    }

    /**
     * @return a collection of default perspectives (i.e. the predefined ones)
     */
    public Collection<ExplorerPerspective> getDefaultPerspectives() {
        ExplorerPerspective classPerspective =
            new ExplorerPerspective(
                "combobox.item.class-centric");
        classPerspective.addRule(new GoProjectToModel());
        classPerspective.addRule(new GoProjectToProfileConfiguration());
        classPerspective.addRule(new GoProfileConfigurationToProfile());
        classPerspective.addRule(new GoProfileToModel());
        //#if defined(COGNITIVE)
        //@#$LPS-COGNITIVE:GranularityType:Statement
        if(org.argouml.specifications.Configuration.COGNITIVE) {
        classPerspective.addRule(new GoProfileToCritics());
        classPerspective.addRule(new GoCriticsToCritic());
        }
        //#endif
        classPerspective.addRule(new GoProjectToRoots());
        classPerspective.addRule(new GoNamespaceToClassifierAndPackage());
        classPerspective.addRule(new GoNamespaceToDiagram());
        classPerspective.addRule(new GoClassToSummary());
        classPerspective.addRule(new GoSummaryToAssociation());
        classPerspective.addRule(new GoSummaryToAttribute());
        classPerspective.addRule(new GoSummaryToOperation());
        classPerspective.addRule(new GoSummaryToInheritance());
        classPerspective.addRule(new GoSummaryToIncomingDependency());
        classPerspective.addRule(new GoSummaryToOutgoingDependency());

        ExplorerPerspective packagePerspective =
            new ExplorerPerspective(
                "combobox.item.package-centric");
        packagePerspective.addRule(new GoProjectToModel());
        packagePerspective.addRule(new GoProjectToProfileConfiguration());
        packagePerspective.addRule(new GoProfileConfigurationToProfile());
        packagePerspective.addRule(new GoProfileToModel());
        //#if defined(COGNITIVE)
        //@#$LPS-COGNITIVE:GranularityType:Statement
        if(org.argouml.specifications.Configuration.COGNITIVE) {
        packagePerspective.addRule(new GoProfileToCritics());
        packagePerspective.addRule(new GoCriticsToCritic());
        }
        //#endif
        packagePerspective.addRule(new GoProjectToRoots());
        packagePerspective.addRule(new GoNamespaceToOwnedElements());
        packagePerspective.addRule(new GoPackageToElementImport());
        packagePerspective.addRule(new GoNamespaceToDiagram());
        packagePerspective.addRule(new GoUseCaseToExtensionPoint());
        packagePerspective.addRule(new GoClassifierToStructuralFeature());
        packagePerspective.addRule(new GoClassifierToBehavioralFeature());
        packagePerspective.addRule(new GoEnumerationToLiterals());
        //#if defined(COLLABORATIONDIAGRAM)
        //@#$LPS-COLLABORATIONDIAGRAM:GranularityType:Statement
        if(org.argouml.specifications.Configuration.COLLABORATIONDIAGRAM) {
        packagePerspective.addRule(new GoCollaborationToInteraction());
        }
        //#endif
        packagePerspective.addRule(new GoInteractionToMessages());
        packagePerspective.addRule(new GoMessageToAction());
        packagePerspective.addRule(new GoSignalToReception());
        packagePerspective.addRule(new GoLinkToStimuli());
        packagePerspective.addRule(new GoStimulusToAction());
        //#if defined(COLLABORATIONDIAGRAM)
        //@#$LPS-COLLABORATIONDIAGRAM:GranularityType:Statement
        if(org.argouml.specifications.Configuration.COLLABORATIONDIAGRAM) {
        packagePerspective.addRule(new GoClassifierToCollaboration());        
        packagePerspective.addRule(new GoOperationToCollaboration());
        }
        //#endif
        packagePerspective.addRule(new GoModelElementToComment());
        //#if defined(COLLABORATIONDIAGRAM)
        //@#$LPS-COLLABORATIONDIAGRAM:GranularityType:Statement
        if(org.argouml.specifications.Configuration.COLLABORATIONDIAGRAM) {
        packagePerspective.addRule(new GoCollaborationToDiagram());
        }
        //#endif
        /*
         * Removed the next one due to issue 2165.
         * packagePerspective.addRule(new GoOperationToCollaborationDiagram());
         */
        packagePerspective.addRule(new GoBehavioralFeatureToStateMachine());
        // works for both statediagram as activitygraph
        //#if defined(STATEDIAGRAM)
        //@#$LPS-STATEDIAGRAM:GranularityType:Statement
        if(org.argouml.specifications.Configuration.STATEDIAGRAM) {
        packagePerspective.addRule(new GoStatemachineToDiagram());
        packagePerspective.addRule(new GoStateMachineToState());
        packagePerspective.addRule(new GoCompositeStateToSubvertex());
        }
        //#endif
        packagePerspective.addRule(new GoStateToInternalTrans());
        packagePerspective.addRule(new GoStateToDoActivity());
        packagePerspective.addRule(new GoStateToEntry());
        packagePerspective.addRule(new GoStateToExit());
        //#if defined(SEQUENCEDIAGRAM)
        //@#$LPS-SEQUENCEDIAGRAM:GranularityType:Statement
        if(org.argouml.specifications.Configuration.SEQUENCEDIAGRAM) {
        packagePerspective.addRule(new GoClassifierToSequenceDiagram());
        packagePerspective.addRule(new GoOperationToSequenceDiagram());
        }
        //#endif
        packagePerspective.addRule(new GoClassifierToInstance());
        //#if defined(STATEDIAGRAM)
        //@#$LPS-STATEDIAGRAM:GranularityType:Statement
        if(org.argouml.specifications.Configuration.STATEDIAGRAM) {
        packagePerspective.addRule(new GoStateToIncomingTrans());
        packagePerspective.addRule(new GoStateToOutgoingTrans());
        }
        //#endif
        packagePerspective.addRule(new GoSubmachineStateToStateMachine());
        packagePerspective.addRule(new GoStereotypeToTagDefinition());
        packagePerspective.addRule(new GoModelElementToBehavior());
        packagePerspective.addRule(new GoModelElementToContainedLostElements());

        ExplorerPerspective diagramPerspective =
            new ExplorerPerspective(
                "combobox.item.diagram-centric");
        diagramPerspective.addRule(new GoProjectToModel());
        diagramPerspective.addRule(new GoProjectToProfileConfiguration());
        diagramPerspective.addRule(new GoProfileConfigurationToProfile());
        diagramPerspective.addRule(new GoProfileToModel());
        //#if defined(COGNITIVE)
        //@#$LPS-COGNITIVE:GranularityType:Statement
        if(org.argouml.specifications.Configuration.COGNITIVE) {
        diagramPerspective.addRule(new GoProfileToCritics());
        diagramPerspective.addRule(new GoCriticsToCritic());
        }
        //#endif
        diagramPerspective.addRule(new GoModelToDiagrams());        
        diagramPerspective.addRule(new GoDiagramToNode());
        diagramPerspective.addRule(new GoDiagramToEdge());
        diagramPerspective.addRule(new GoUseCaseToExtensionPoint());
        diagramPerspective.addRule(new GoClassifierToStructuralFeature());
        diagramPerspective.addRule(new GoClassifierToBehavioralFeature());

        ExplorerPerspective inheritancePerspective =
            new ExplorerPerspective(
                "combobox.item.inheritance-centric");
        inheritancePerspective.addRule(new GoProjectToModel());
        inheritancePerspective.addRule(new GoProjectToProfileConfiguration());
        classPerspective.addRule(new GoProfileConfigurationToProfile());
        classPerspective.addRule(new GoProfileToModel());
        //#if defined(COGNITIVE)
        //@#$LPS-COGNITIVE:GranularityType:Statement
        if(org.argouml.specifications.Configuration.COGNITIVE) {
        classPerspective.addRule(new GoProfileToCritics());
        classPerspective.addRule(new GoCriticsToCritic());
        }
        //#endif
        inheritancePerspective.addRule(new GoModelToBaseElements());
        inheritancePerspective
                .addRule(new GoGeneralizableElementToSpecialized());

        ExplorerPerspective associationsPerspective =
            new ExplorerPerspective(
                "combobox.item.class-associations");
        associationsPerspective.addRule(new GoProjectToModel());
        associationsPerspective.addRule(new GoProjectToProfileConfiguration());
        associationsPerspective.addRule(new GoProfileConfigurationToProfile());
        associationsPerspective.addRule(new GoProfileToModel());
        //#if defined(COGNITIVE)
        //@#$LPS-COGNITIVE:GranularityType:Statement
        if(org.argouml.specifications.Configuration.COGNITIVE) {
        associationsPerspective.addRule(new GoProfileToCritics());
        associationsPerspective.addRule(new GoCriticsToCritic());
        }
        //#endif
        associationsPerspective.addRule(new GoNamespaceToDiagram());
        associationsPerspective.addRule(new GoPackageToClass());
        associationsPerspective.addRule(new GoClassToAssociatedClass());

        ExplorerPerspective residencePerspective =
            new ExplorerPerspective(
                "combobox.item.residence-centric");
        residencePerspective.addRule(new GoProjectToModel());
        residencePerspective.addRule(new GoProjectToProfileConfiguration());
        residencePerspective.addRule(new GoProfileConfigurationToProfile());
        residencePerspective.addRule(new GoProfileToModel());
        //#if defined(COGNITIVE)
        //@#$LPS-COGNITIVE:GranularityType:Statement
        if(org.argouml.specifications.Configuration.COGNITIVE) {
        residencePerspective.addRule(new GoProfileToCritics());
        residencePerspective.addRule(new GoCriticsToCritic());
        }
        //#endif
        residencePerspective.addRule(new GoModelToNode());
        residencePerspective.addRule(new GoNodeToResidentComponent());
        residencePerspective.addRule(new GoComponentToResidentModelElement());
        //#if defined(STATEDIAGRAM)
        //@#$LPS-STATEDIAGRAM:GranularityType:Statement
        ExplorerPerspective statePerspective =null;
        if(org.argouml.specifications.Configuration.STATEDIAGRAM) {
        statePerspective =
            new ExplorerPerspective(
                "combobox.item.state-centric");
        statePerspective.addRule(new GoProjectToStateMachine());
        statePerspective.addRule(new GoStatemachineToDiagram());
        statePerspective.addRule(new GoStateMachineToState());
        statePerspective.addRule(new GoCompositeStateToSubvertex());
        statePerspective.addRule(new GoStateToIncomingTrans());
        statePerspective.addRule(new GoStateToOutgoingTrans());
        statePerspective.addRule(new GoTransitiontoEffect());
        statePerspective.addRule(new GoTransitionToGuard());
        }
        //#endif
        ExplorerPerspective transitionsPerspective =
            new ExplorerPerspective(
                "combobox.item.transitions-centric");
        //#if defined(STATEDIAGRAM)
        //@#$LPS-STATEDIAGRAM:GranularityType:Statement
        if(org.argouml.specifications.Configuration.STATEDIAGRAM) {
        transitionsPerspective.addRule(new GoProjectToStateMachine());
        transitionsPerspective.addRule(new GoStatemachineToDiagram());
        transitionsPerspective.addRule(new GoStateMachineToTransition());
        }
        //#endif
        transitionsPerspective.addRule(new GoTransitionToSource());
        transitionsPerspective.addRule(new GoTransitionToTarget());
        transitionsPerspective.addRule(new GoTransitiontoEffect());
        transitionsPerspective.addRule(new GoTransitionToGuard());

        ExplorerPerspective compositionPerspective =
            new ExplorerPerspective(
                "combobox.item.composite-centric");
        compositionPerspective.addRule(new GoProjectToModel());
        compositionPerspective.addRule(new GoProjectToProfileConfiguration());
        compositionPerspective.addRule(new GoProfileConfigurationToProfile());
        compositionPerspective.addRule(new GoProfileToModel());
        //#if defined(COGNITIVE)
        //@#$LPS-COGNITIVE:GranularityType:Statement
        if(org.argouml.specifications.Configuration.COGNITIVE) {
        compositionPerspective.addRule(new GoProfileToCritics());
        compositionPerspective.addRule(new GoCriticsToCritic());
        }
        //#endif
        compositionPerspective.addRule(new GoProjectToRoots());
        compositionPerspective.addRule(new GoModelElementToContents());
        compositionPerspective.addRule(new GoModelElementToContainedDiagrams());

        Collection<ExplorerPerspective> c = 
            new ArrayList<ExplorerPerspective>();
        c.add(packagePerspective);
        c.add(classPerspective);
        c.add(diagramPerspective);
        c.add(inheritancePerspective);
        c.add(associationsPerspective);
        c.add(residencePerspective);
        //#if defined(STATEDIAGRAM)
        //@#$LPS-STATEDIAGRAM:GranularityType:Statement
        if(org.argouml.specifications.Configuration.STATEDIAGRAM) {
        c.add(statePerspective);
        }//#endif
        c.add(transitionsPerspective);
        c.add(compositionPerspective);
        return c;
    }

    /**
     * Get the predefined rules.<p>
     *
     * This is a hard coded rules library for now, since it is quite a lot of
     * work to get all possible rule names in "org.argouml.ui.explorer.rules"
     * from the classpath (which would also not allow adding rules from other
     * locations).
     */
    public void loadRules() {

    	
        
//        	PerspectiveRule[] ruleNamesArray = {new GoAssocRoleToMessages(),
//                    //#if defined(STATEDIAGRAM)
//                    //@#$LPS-STATEDIAGRAM:GranularityType:StaticInitialization
//                     new GoBehavioralFeatureToStateDiagram(),
//                    //#endif
//                    new GoBehavioralFeatureToStateMachine(),
//                    new GoClassifierToBehavioralFeature(),
//                    //#if defined(COLLABORATIONDIAGRAM)
//                    //@#$LPS-COLLABORATIONDIAGRAM:GranularityType:StaticInitialization
//                     new GoClassifierToCollaboration(),
//                    //#endif
//                    new GoClassifierToInstance(),
//                    //#if defined(SEQUENCEDIAGRAM)
//                    //@#$LPS-SEQUENCEDIAGRAM:GranularityType:StaticInitialization
//                    new GoClassifierToSequenceDiagram(),
//                    //#endif
//                    //#if defined(STATEDIAGRAM)
//                    //@#$LPS-STATEDIAGRAM:GranularityType:StaticInitialization
//                    new GoClassifierToStateMachine(),
//                    //#endif
//                    new GoClassifierToStructuralFeature(),
//                    new GoClassToAssociatedClass(), new GoClassToNavigableClass(),
//                    new GoClassToSummary(), 
//                    //#if defined(COLLABORATIONDIAGRAM)
//                    //@#$LPS-COLLABORATIONDIAGRAM:GranularityType:StaticInitialization
//                    new GoCollaborationToDiagram(),            
//                    new GoCollaborationToInteraction(),
//                    //#endif
//                    new GoComponentToResidentModelElement(),
//                    //#if defined(STATEDIAGRAM)
//                    //@#$LPS-STATEDIAGRAM:GranularityType:StaticInitialization
//                    new GoCompositeStateToSubvertex(), new GoDiagramToEdge(),
//                    //#endif
//                    new GoDiagramToNode(), new GoElementToMachine(),
//                    new GoEnumerationToLiterals(),
//                    new GoGeneralizableElementToSpecialized(),
//                    new GoInteractionToMessages(), new GoLinkToStimuli(),
//                    new GoMessageToAction(), new GoModelElementToComment(),
//                    new GoModelElementToBehavior(),
//                    new GoModelElementToContents(),
//                    new GoModelElementToContainedDiagrams(),
//                    new GoModelElementToContainedLostElements(),
//                    new GoModelToBaseElements(),
//                    //#if defined(COLLABORATIONDIAGRAM)
//                    //@#$LPS-COLLABORATIONDIAGRAM:GranularityType:StaticInitialization
//                    new GoModelToCollaboration(),
//                    //#endif
//                    new GoModelToDiagrams(), new GoModelToElements(),
//                    new GoModelToNode(), new GoNamespaceToClassifierAndPackage(),
//                    new GoNamespaceToDiagram(), new GoNamespaceToOwnedElements(),
//                    new GoNodeToResidentComponent(),            
//                    //#if defined(COLLABORATIONDIAGRAM)
//                    //@#$LPS-COLLABORATIONDIAGRAM:GranularityType:StaticInitialization
//                    new GoOperationToCollaborationDiagram(),
//                    new GoOperationToCollaboration(),
//                    //#endif
//                    //#if defined(SEQUENCEDIAGRAM)
//                    //@#$LPS-SEQUENCEDIAGRAM:GranularityType:StaticInitialization
//                    new GoOperationToSequenceDiagram(), new GoPackageToClass(),
//                    //#endif
//                    new GoPackageToElementImport(),
//                    //#if defined(COLLABORATIONDIAGRAM)
//                    //@#$LPS-COLLABORATIONDIAGRAM:GranularityType:StaticInitialization
//                    new GoProjectToCollaboration(),
//                    //#endif
//                    new GoProjectToDiagram(),
//                    //#if defined(STATEDIAGRAM)
//                    //@#$LPS-STATEDIAGRAM:GranularityType:StaticInitialization
//                    new GoProjectToModel(), new GoProjectToStateMachine(),
//                    //#endif
//                    new GoProjectToProfileConfiguration(), 
//                    new GoProfileConfigurationToProfile(),
//                    new GoProfileToModel(),
//                    //#if defined(COGNITIVE)
//                    //@#$LPS-COGNITIVE:GranularityType:StaticInitialization
//                    new GoProfileToCritics(),
//                    new GoCriticsToCritic(),
//                    //#endif
//                    new GoProjectToRoots(),
//                    //#if defined(STATEDIAGRAM)
//                    //@#$LPS-STATEDIAGRAM:GranularityType:StaticInitialization
//                    new GoSignalToReception(), new GoStateMachineToTop(),
//                    new GoStatemachineToDiagram(), new GoStateMachineToState(),
//                    new GoStateMachineToTransition(), new GoStateToDoActivity(),
//                    //#endif
//                    new GoStateToDownstream(), new GoStateToEntry(),
//                    //#if defined(STATEDIAGRAM)
//                    //@#$LPS-STATEDIAGRAM:GranularityType:StaticInitialization
//                    new GoStateToExit(), new GoStateToIncomingTrans(),
//                    new GoStateToInternalTrans(), new GoStateToOutgoingTrans(),
//                    //#endif
//                    new GoStereotypeToTagDefinition(),
//                    new GoStimulusToAction(), new GoSummaryToAssociation(),
//                    new GoSummaryToAttribute(),
//                    new GoSummaryToIncomingDependency(),
//                    new GoSummaryToInheritance(), new GoSummaryToOperation(),
//                    new GoSummaryToOutgoingDependency(),
//                    new GoTransitionToSource(), new GoTransitionToTarget(),
//                    new GoTransitiontoEffect(), new GoTransitionToGuard(),
//                    new GoUseCaseToExtensionPoint(),
//                    new GoSubmachineStateToStateMachine(),
//                };
        	
        	int cont=0;
        	int size=51;
        	
        	if(org.argouml.specifications.Configuration.STATEDIAGRAM) {
        		size+=16;
        	}
        	if(org.argouml.specifications.Configuration.COLLABORATIONDIAGRAM) {
        		size+=7;
        	}
        	if(org.argouml.specifications.Configuration.SEQUENCEDIAGRAM) {
        		size+=3;
        	}
        	if(org.argouml.specifications.Configuration.COGNITIVE) {
        		size+=2;
        	}
        	
        	
        	PerspectiveRule[] ruleNamesArray = new PerspectiveRule[size] ;
        	
        	ruleNamesArray[cont]=new GoAssocRoleToMessages();
        	cont++;
        	
        	
        	if(org.argouml.specifications.Configuration.STATEDIAGRAM) {
        		ruleNamesArray[cont]=	new GoBehavioralFeatureToStateDiagram();
        		cont++;
        	}
        	
        	ruleNamesArray[cont]=new GoBehavioralFeatureToStateMachine();
        	cont++;
        	ruleNamesArray[cont]=new GoClassifierToBehavioralFeature();
        	cont++;

        	if(org.argouml.specifications.Configuration.COLLABORATIONDIAGRAM) {
        		ruleNamesArray[cont]=new GoClassifierToCollaboration();
        		cont++;
        	}
        	ruleNamesArray[cont]=new GoClassifierToInstance();
        	cont++;
        	
        	if(org.argouml.specifications.Configuration.SEQUENCEDIAGRAM) {
        		ruleNamesArray[cont]=new GoClassifierToSequenceDiagram();
        		cont++;
        	}
        	
        	if(org.argouml.specifications.Configuration.STATEDIAGRAM) {
        		ruleNamesArray[cont]=new GoClassifierToStateMachine();
        		cont++;
        	}
        	
        	ruleNamesArray[cont]= new GoClassifierToStructuralFeature();
        	cont++;
        	ruleNamesArray[cont]= new GoClassToAssociatedClass();
        	cont++;
        	ruleNamesArray[cont]=new GoClassToNavigableClass();
        	cont++;
        	ruleNamesArray[cont]= new GoClassToSummary(); 
        	cont++;
        	
        	if(org.argouml.specifications.Configuration.COLLABORATIONDIAGRAM) {
        		ruleNamesArray[cont]=new GoCollaborationToDiagram();
        		cont++;
        		ruleNamesArray[cont]=new GoCollaborationToInteraction();
        		cont++;
        	}

        	ruleNamesArray[cont]= new GoComponentToResidentModelElement();
        	cont++;
        	
        	if(org.argouml.specifications.Configuration.STATEDIAGRAM) {
        		ruleNamesArray[cont]=new GoCompositeStateToSubvertex();
        		cont++;
        		ruleNamesArray[cont]=new GoDiagramToEdge();
        		cont++;
        	}
        	
        	
        	ruleNamesArray[cont]=new GoDiagramToNode();
        	cont++;
        	ruleNamesArray[cont]=new GoElementToMachine();
        	cont++;
        	ruleNamesArray[cont]=new GoEnumerationToLiterals();
        	cont++;
        	ruleNamesArray[cont]=new GoGeneralizableElementToSpecialized();
        	cont++;
        	ruleNamesArray[cont]=new GoInteractionToMessages();
        	cont++;
        	ruleNamesArray[cont]=new GoLinkToStimuli();
        	cont++;
        	ruleNamesArray[cont]=new GoMessageToAction();
        	cont++;
        	ruleNamesArray[cont]=new GoModelElementToComment();
        	cont++;
        	ruleNamesArray[cont]=new GoModelElementToBehavior();
        	cont++;
        	ruleNamesArray[cont]=new GoModelElementToContents();
        	cont++;
        	ruleNamesArray[cont]=new GoModelElementToContainedDiagrams();
        	cont++;
        	ruleNamesArray[cont]=new GoModelElementToContainedLostElements();
        	cont++;
        	ruleNamesArray[cont]=new GoModelToBaseElements();
        	cont++;
        	
        	if(org.argouml.specifications.Configuration.COLLABORATIONDIAGRAM) {
        		ruleNamesArray[cont]=new GoModelToCollaboration();
        		cont++;
        	}
        	ruleNamesArray[cont]=new GoModelToDiagrams();
        	cont++;
        	ruleNamesArray[cont]=new GoModelToElements();
        	cont++;
        	ruleNamesArray[cont]=new GoModelToNode();
        	cont++;
        	ruleNamesArray[cont]=new GoNamespaceToClassifierAndPackage();
        	cont++;
        	ruleNamesArray[cont]=new GoNamespaceToDiagram();
        	cont++;
        	ruleNamesArray[cont]=new GoNamespaceToOwnedElements();
        	cont++;
        	ruleNamesArray[cont]=new GoNodeToResidentComponent();            
        	cont++;
        	
        	if(org.argouml.specifications.Configuration.COLLABORATIONDIAGRAM) {
        		  ruleNamesArray[cont]=new GoOperationToCollaborationDiagram();
        		  cont++;
               	  ruleNamesArray[cont]=new GoOperationToCollaboration();
               	  cont++;
            }
        	if(org.argouml.specifications.Configuration.SEQUENCEDIAGRAM) {
      		  ruleNamesArray[cont]=new GoOperationToSequenceDiagram();
      		  cont++;
      		  ruleNamesArray[cont]=new GoPackageToClass();
      		  cont++;
            }
        	
        	ruleNamesArray[cont]=new GoPackageToElementImport();            
        	cont++;
        	
        	if(org.argouml.specifications.Configuration.COLLABORATIONDIAGRAM) {
        		  ruleNamesArray[cont]=new GoProjectToCollaboration();
        		  cont++;
              }
        	
        	
        	ruleNamesArray[cont]=new GoProjectToDiagram();            
        	cont++;
        	
        	if(org.argouml.specifications.Configuration.STATEDIAGRAM) {
        		ruleNamesArray[cont]=new GoProjectToModel();
        		cont++;
      		    ruleNamesArray[cont]=new GoProjectToStateMachine();
      		    cont++;
            }
      
        	ruleNamesArray[cont]=new GoProjectToProfileConfiguration();            
        	cont++;
        	ruleNamesArray[cont]=new GoProfileConfigurationToProfile();            
        	cont++;
        	ruleNamesArray[cont]=new GoProfileToModel();            
        	cont++;
        	
        	if(org.argouml.specifications.Configuration.COGNITIVE) {
        		ruleNamesArray[cont]=new GoProfileToCritics();
        		cont++;
      		    ruleNamesArray[cont]=new GoCriticsToCritic();
      		    cont++;
            }
        	ruleNamesArray[cont]=new GoProjectToRoots();            
        	cont++;
        	if(org.argouml.specifications.Configuration.STATEDIAGRAM) {
        		ruleNamesArray[cont]=new GoSignalToReception();
        		cont++;
        		ruleNamesArray[cont]=new GoStateMachineToTop();
        		cont++;
        		ruleNamesArray[cont]=new GoStatemachineToDiagram();
        		cont++;
        		ruleNamesArray[cont]=new GoStateMachineToState();
        		cont++;
        		ruleNamesArray[cont]=new GoStateMachineToTransition(); 
        		cont++;
        		ruleNamesArray[cont]=new GoStateToDoActivity();
        		cont++;
        	}
        	ruleNamesArray[cont]=new GoStateToDownstream();
        	cont++;
        	ruleNamesArray[cont]=new GoStateToEntry();
        	cont++;
        	if(org.argouml.specifications.Configuration.STATEDIAGRAM) {
        		ruleNamesArray[cont]=new GoStateToExit();
        		cont++;
        		ruleNamesArray[cont]= new GoStateToIncomingTrans();
        		cont++;
        		ruleNamesArray[cont]=new GoStateToInternalTrans();
        		cont++;
        		ruleNamesArray[cont]=new GoStateToOutgoingTrans();
        		cont++;
        	}
                	
        	ruleNamesArray[cont]=new GoStereotypeToTagDefinition();
        	cont++;
        	ruleNamesArray[cont]= new GoStimulusToAction(); 
        	cont++;
        	ruleNamesArray[cont]=new GoSummaryToAssociation();
        	cont++;
        	ruleNamesArray[cont]=new GoSummaryToAttribute();
        	cont++;
        	ruleNamesArray[cont]=new GoSummaryToIncomingDependency();
        	cont++;
        	ruleNamesArray[cont]=new GoSummaryToInheritance(); 
        	cont++;
        	ruleNamesArray[cont]=new GoSummaryToOperation();
        	cont++;
        	ruleNamesArray[cont]=new GoSummaryToOutgoingDependency();
        	cont++;
        	ruleNamesArray[cont]=new GoTransitionToSource();
        	cont++;
        	ruleNamesArray[cont]=new GoTransitionToTarget();
        	cont++;
        	ruleNamesArray[cont]=new GoTransitiontoEffect(); 
        	cont++;
        	ruleNamesArray[cont]=new GoTransitionToGuard();
        	cont++;
        	ruleNamesArray[cont]=new GoUseCaseToExtensionPoint();
        	cont++;
        	ruleNamesArray[cont]=new GoSubmachineStateToStateMachine();
        	
        	
        	rules = Arrays.asList(ruleNamesArray);
        	
            }

    /**
     * Add a rule to the list of rules.
     *
     * @param rule
     *            the PerspectiveRule to be added
     */
    public void addRule(PerspectiveRule rule) {
        rules.add(rule);
    }

    /**
     * Remove a rule from the list.
     *
     * @param rule
     *            the PerspectiveRule to be removed
     */
    public void removeRule(PerspectiveRule rule) {
        rules.remove(rule);
    }

    /**
     * @return the collection of rules
     */
    public Collection<PerspectiveRule> getRules() {
        return rules;
    }

    /**
     * Save the user perspectives in the ArgoUML configuration.
     */
    public void saveUserPerspectives() {
        Configuration.setString(Argo.KEY_USER_EXPLORER_PERSPECTIVES, this
                .toString());
    }

    /**
     * @return string representation of the perspectives in the same format as
     *         saved in the user properties.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        StringBuffer p = new StringBuffer();

        for (ExplorerPerspective perspective : getPerspectives()) {
            String name = perspective.toString();
            p.append(name).append(",");
            for (PerspectiveRule rule : perspective.getList()) {
                p.append(rule.getClass().getName()).append(",");
            }
            p.deleteCharAt(p.length() - 1);
            p.append(";");
        }
        
        p.deleteCharAt(p.length() - 1);
        return p.toString();
    }
}
