cd build

java LEGv8Disassembler $1

if [ $? -eq 0 ]; then
  echo "Execution successful."
else
  echo "Execution failed."
fi