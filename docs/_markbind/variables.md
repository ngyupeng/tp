<variable name="example">
To inject this HTML segment in your markbind files, use {{ example }} where you want to place it.
More generally, surround the segment's id with double curly braces.
</variable>

<br><br>

<!-- Shows a popup for instructions on verifying the installed Java version. -->
<variable name="verifyJava17Box">
<box type="info" dismissible>
How do I check that it's installed properly?

1. On **Windows**, Press `Win + R`, type `cmd` and press Enter.<br>
   On **Mac**, Open `Terminal` from Applications.<br>
2. Type `java --version` and press Enter. <br>
3. The message should show Java 17 installed (see below).

<img src="images/verify-java-17.png" alt="Image showing Java 17 installed" width="750" height="100"/>
</box>
</variable>

<!-- Shows a tooltip for instructions on navigating in the terminal. -->
<variable name="navigateGuideBox">
<box type="info" dismissible>
How do I navigate using my terminal? See below:<br>
   - <a href="https://www.lifewire.com/change-directories-in-command-prompt-5185508">
   Guide to navigating on <b>Windows</b></a> <br>
   - <a href="https://gomakethings.com/navigating-the-file-system-with-terminal/">
   Guide to navigating on <b>Mac</b></a>
</box>
</variable>

