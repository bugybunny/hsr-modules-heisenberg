/*
	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.hsr.modules.uint1.heisenberglibrary.view;

import java.awt.Dialog;
import java.awt.Frame;

/**
 * 
 * @author msyfrig
 */
public class AbstractTabbedPaneDialog extends NonModalJDialog {

    /**
     * Creates a new instance of this class.
     * 
     * @param anOwner
     * @param aTitle
     */
    public AbstractTabbedPaneDialog(Frame anOwner, String aTitle) {
        super(anOwner, aTitle);
        // TODO Auto-generated constructor stub
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param aAnOwner
     * @param aTitle
     */
    public AbstractTabbedPaneDialog(Dialog aAnOwner, String aTitle) {
        super(aAnOwner, aTitle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void initComponents() {
        // TODO Auto-generated method stub

    }

}
