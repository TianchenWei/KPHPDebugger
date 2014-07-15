package org.phpsemantics.debug.core.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant;
import org.phpsemantics.debug.core.model.KPHPStackFrame;

public class KPHPSourceLookupParticipant extends AbstractSourceLookupParticipant{

	@Override
	public String getSourceName(Object object) throws CoreException {
		// TODO change getName()
		if (object instanceof KPHPStackFrame) {
		       return ((KPHPStackFrame)object).getName();
		       }
		       return null;
	}

}
