// Copyright (C) 2002 by Jason Hunter <jhunter_AT_acm_DOT_org>.
// All rights reserved.  Use of this class is limited.
// Please see the LICENSE for more information.

package com.domeke.app.cos.multipart;

import java.io.File;
import java.util.UUID;

/**
 * Implements a renaming policy that adds increasing integers to the body of any
 * file that collides. For example, if foo.gif is being uploaded and a file by
 * the same name already exists, this logic will rename the upload foo1.gif. A
 * second upload by the same name would be foo2.gif. Note that for safety the
 * rename() method creates a zero-length file with the chosen name to act as a
 * marker that the name is taken even before the upload starts writing the
 * bytes.
 * 
 * @author Jason Hunter
 * @version 1.1, 2002/11/05, making thread safe with createNewFile()
 * @version 1.0, 2002/04/30, initial revision, thanks to Yoonjung Lee for this
 *          idea
 */
public class DefaultFileRenamePolicy implements FileRenamePolicy {

	// This method does not need to be synchronized because createNewFile()
	// is atomic and used here to mark when a file name is chosen
	public File rename(File f) {
		String name = f.getName();
		String ext = null;

		int dot = name.lastIndexOf(".");
		if (dot != -1) {
			ext = name.substring(dot); // includes "."
		} else {
			ext = "";
		}
		UUID uuid = UUID.randomUUID();
		String newName = uuid.toString() + ext;
		f = new File(f.getParent(), newName);
		return f;
	}
}
