## bandit write-up by Amish

level 0

	ssh bandit0@<address> -p 2220
	and entered password :	
	bandit0


level 1

	cat readme
	found password	
	boJ9jbbUNNfktd78OOpsqOltutMc3MY1	 				

level 2

	cat ./-
	found password	
	CV1DtqXWVFXTvM2F0k09SHz0YwRINYA9

level 3	

	cat 'spaces in this filename'
	found pass
	UmHadQclWmgdLOKQ3YNgjWxGoRMb5luK
	
level 4

	first i used ls -a and then 	
	cat ./inhere/.hidden

level 5

 	used file ./-* to display the types of all files
	found only one text file
	cat ./-file07

level 6

	find . -size 1033c , to search for the file with size 1033 bytes
	matched one file 
	cat ./inhere/maybehere07/.file2
		
level 7

	find / -size 33c -user bandit7 -group bandit6, search with specified params
	matched one file
	cat /var/lib/dpkg/info/bandit7.password

level 8

	using
	grep millionth data.txt
	displayed the line containing millionth in data.txt

level 9

	used piping for first time, first sorted then found uniq entry
	sort data.txt | uniq -u

level 10

	used strings, and then searched for == in data.txt, one entry looked like password
	strings data.txt | grep ==
	
level 11

	base64 -d for decoding 
	base64 -d < data.txt

level 12

	wrote translation by 13 places using tr
	cat data.txt | tr [A-Za-z] [N-ZA-Mn-za-m]

level 13

	very frustrating, the problem designer got carried away with repeatedly compressing the 	files
	used xxd -r command to revert hexdump
	each time i had keep track of the file type by using 
	file <name-of-file>
	and renaming file to appropriate extension
	and then using gunzip/bunzip2/tar -x -O		
	until i found ASCII text file containg password

	much easier approach is to
	chain pipeline everything appending next command in each step
	cat data.txt | xxd -r |gunzip|bzcat|gunzip|tar -x -O|tar -x -O|bzcat|tar -x -O|gunzip | 	file -
	we have file - to tell us file type everytime
	cat data.txt | xxd -r |gunzip|bzcat|gunzip|tar -x -O|tar -x -O|bzcat|tar -x -O|gunzip
	this procedure saves time,	

