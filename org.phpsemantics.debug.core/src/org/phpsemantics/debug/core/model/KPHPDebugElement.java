package org.phpsemantics.debug.core.model;

import org.eclipse.debug.core.model.DebugElement;
import org.eclipse.debug.core.model.IDebugTarget;

public abstract class KPHPDebugElement extends DebugElement{

	public KPHPDebugElement(IDebugTarget target) {
		super(target);
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IDebugElement#getModelIdentifier()
	 */
	public String getModelIdentifier() {
		return null;
		//return IKPHPConstants.ID_PDA_DEBUG_MODEL;
	}
	
}
