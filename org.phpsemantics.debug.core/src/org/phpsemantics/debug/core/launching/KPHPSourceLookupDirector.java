package org.phpsemantics.debug.core.launching;

import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupDirector;
import org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant;

public class KPHPSourceLookupDirector extends AbstractSourceLookupDirector {

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.sourcelookup.ISourceLookupDirector#initializeParticipants()
	 */
	public void initializeParticipants() {
	    addParticipants(new ISourceLookupParticipant[]{new KPHPSourceLookupParticipant()});
	}

}
