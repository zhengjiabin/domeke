<?php

// Echo the image - timestamp appended to prevent caching
echo '<a href="index.php" onclick="refreshimg(); return false;" title="Click to refresh image"><img src="http://www.dongmark.com/images/image.jpg?' . time() . '" width="132" height="46" alt="Captcha image" /></a>';

?>