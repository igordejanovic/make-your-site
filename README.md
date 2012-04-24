make-your-site
==============

Kids tool for web site creation based on simple visual language for site structure and page content definition.

Written as the demonstration of the Eclipse RCP, SWT/JFace and Zest2 tehnologies
that are used at the ["Software Patterns and Components"](http://www.informatika.ftn.uns.ac.rs/SOK) course at the Chair for Informatics, Faculty of Technical Sciences, University of Novi Sad.

This is a work in progress.


Installation
============

TODO

Source checkout
===============

```
git clone git://github.com/igordejanovic/make-your-site.git
cd make-your-site
git submodule init
git submodule update
```

Target Platform
===============

Eclipse target platform for this project is hosted [here](http://puppet.ftn.uns.ac.rs/materijali/Eclipse-RCP-3.7.2-SOK-TargetPlatform.zip).

Unpack it and create eclipse variable TARGET_ROOT (Window->Preferences->Run/Debug->String Supstitution) that points to the target platform folder.
Switch to the target platform defined in the make-your-site project (Window->Preferences->Plug-in Development->Target Platform).


License
=======

Licensed under the terms of Eclipse Public License.


Credits
=======

This software uses following open-source libs/projects/tehnologies:

    * Eclipse RCP
    * Zest
    * LeetEdit
    * TinyMCE (integrated by LeetEdit)

TODO
====

 * Site creation (HTML+CSS code generation).
 * Load/Save
 * English localization.
 * Javadocs.
