import java.awt.event.KeyEvent;

    public enum SIControls
    {
        P1_MOVE_RIGHT (KeyEvent.VK_D),
        P1_MOVE_LEFT  (KeyEvent.VK_A),
        P1_FIRE       (KeyEvent.VK_W),
        P2_MOVE_RIGHT (KeyEvent.VK_RIGHT),
        P2_MOVE_LEFT  (KeyEvent.VK_LEFT),
        P2_FIRE       (KeyEvent.VK_UP);
    
        private int keyCode;
    
        private SIControls(int key)
        {
            keyCode = key;
        }
        
        public int getKeyCode()
        {
            return keyCode;
        }
        
        public String getKeyText()
        {
            return KeyEvent.getKeyText(keyCode).toUpperCase();
        }
        
        public boolean equals(KeyEvent key)
        {
            return keyCode == key.getKeyCode();
        }
    }
