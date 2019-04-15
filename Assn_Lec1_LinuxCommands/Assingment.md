## Linux Commands Assignment  
 
 
#### level 0
- made ssh using the command  `ssh bandit0@bandit.labs.overthewire.org -p 2220`
 
#### level 0 - level 1
- the password obtained is `boJ9jbbUNNfktd78OOpsqOltutMc3MY1`
 
#### level 1 - level 2
- used command 'cat /-' and obtained the password `CV1DtqXWVFXTvM2F0k09SHz0YwRINYA9`
 
#### level 2 - level 3
- used command `cat "spaces in this filename"` 
- obtained the password `UmHadQclWmgdLOKQ3YNgjWxGoRMb5luK`
 
#### level 3 - level 4
- used command `cd inhere` 
- then used command `ls -a` 
- then got file name as `.hidden`
- used `cat .hidden` to get password `pIwrPrtPN36QITSp3EQaw936yaFoFgAB`
 
#### level 4 -level 5 
- used command `cd inhere` 
- then used command `ls` 
- then `cat ./-[filename]` to print content the readable file had 'koReBOKuIDDepwhWk7jZC0RTdopnAYKh` password
 
#### level 5 - level 6
- used command `find /home/ -size 1033c -exec ls {} \;` to find the file 
- the file was `.file2` in `inhere/maybehere07` along with some files showing `permission denied`
- password was obtained by `cat` `DXjZPULLxYr17uwoI01bNLQbtFemEgo7` 
 
#### level 6 - level 7
- used `find` command along with the `-group` command to find the file which was name `bandit7.password` inside of `var/lib/dpkg/info`
- password received was `HKBPTKQnIay4Fw76bEy8PVxKEDQRKTzs`
 
#### level 7 - level 8
- used command `grep millionth data.txt` 
- gave the password `cvX2JJa4CFALtqS87jk27qwqGhBM9plV`
 
#### level 8 - level 9
- used command `sort data.txt | uniq -u` 
- it gave the password `UsvVyFSfZZWbi6wgC7dAFyFuR6jQQUhR`
 
#### level 9 - level 10
- used command `string data.txt` to get the usable string
- then used `strings data.txt | grep =` 
- it gave three possible passwords 1- `password` 2- `isa` 3- `truKLdjsbJ5g7yyJ2X2R0o3a5HQJFuLk` the last one worked on bandit10
 
#### level 10 - level 11
- base64 encoding is quite cool read complete doc binary conversion form 8 bit to 6 bit used command `base64 -d data.txt` 
- it gave the password `IFukwKGsFW8MOq3IRFqrxE1hxTNEbUPR`
 
#### level 11 - level 12
- Learned to use `tr` and `alias` used command ` alias rot13="tr 'A-Za-z' 'N-ZA-Mn-za-m'" ` 
- then used command `cat data.txt | rot13` 
- or directly used  `cat data.txt |tr 'A-Za-z' 'N-ZA-Mn-za-m'` 
- found the password `5Te8Y4drgCRfCx8ugdwuEX8KFC6k2EUu`
 
#### level 12 - level 13
	
	
	


