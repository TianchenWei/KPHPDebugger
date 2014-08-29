package org.phpsemantics.debug.core.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant;
import org.phpsemantics.debug.core.model.KPHPStackFrame;

public class KPHPSourceLookupParticipant extends AbstractSourceLookupParticipant{

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant#getSourceName(Object object)
	 */
	public String getSourceName(Object object) throws CoreException {
		if (object instanceof KPHPStackFrame) {
		    return ((KPHPStackFrame)object).getDebugTarget().getName();
		}
		return null;
	}

}
