/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.jasperreports.data;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelObject;


/**
 * ViewChildrenDataSource
 * 
 * @author Phillip Beauvoir
 */
public class ViewChildrenDataSource implements JRRewindableDataSource {
    
    private List<IArchimateElement> fChildren = new ArrayList<IArchimateElement>();
    private IArchimateElement fCurrentElement;
    private int currentIndex = -1;

    public ViewChildrenDataSource(IDiagramModel dm) {
        getAllChildObjects(dm);
        ArchimateModelDataSource.sort(fChildren);
    }
    
    private void getAllChildObjects(IDiagramModelContainer container) {
        for(IDiagramModelObject child : container.getChildren()) {
            if(child instanceof IDiagramModelArchimateObject) {
                IArchimateElement element = ((IDiagramModelArchimateObject)child).getArchimateElement();
                if(element != null && !fChildren.contains(element)) {
                    fChildren.add(element);
                }
            }
            
            if(child instanceof IDiagramModelContainer) {
                getAllChildObjects((IDiagramModelContainer)child);
            }
        }
    }

    @Override
    public boolean next() throws JRException {
        if(currentIndex < fChildren.size() - 1) {
            fCurrentElement = fChildren.get(++currentIndex);
        }
        else {
            fCurrentElement = null;
        }
        
        return fCurrentElement != null;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        String fieldName = jrField.getName();
        
        if("name".equals(fieldName)) {
            return fCurrentElement.getName();
        }
        if("type".equals(fieldName)) {
            return ArchimateNames.getDefaultName(fCurrentElement.eClass());
        }
        return null;
    }

    @Override
    public void moveFirst() throws JRException {
        currentIndex = -1;
    }

}
