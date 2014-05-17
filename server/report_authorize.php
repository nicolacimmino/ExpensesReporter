<?php
/* report_authorize.php is part of ExpensesReporter and is responsible to
 *   accept requests to authenticate a user and provide an authorization token.
 *
 *   Copyright (C) 2014 Nicola Cimmino
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see http://www.gnu.org/licenses/.
 *
*/
	// Ignore anything that is not POST requests, we don't implement anything 
	//	else for now.
	if($_SERVER['REQUEST_METHOD']!="POST")
	{
		exit;
	}
	
	// Extract params from the request. We just ignore the request if some
	//	critical parameters are missing. This is not the best choice but since
	//  this API is meant only for our application consumption a nice error 
	// 	won't help the application sending different data at the next sync.
	// For a public API a clear error would be better choices so that users
	//	have an easier time integrating their applications.
	$name =(isset($_REQUEST['name']))?$_REQUEST['name']:0;
	$password =(isset($_REQUEST['password']))?$_REQUEST['password']:"";
	$type =(isset($_REQUEST['type']))?$_REQUEST['type']:"";
				
	// Get a database connection
	require_once("pdo_intra.php");
	
	// Find a user by that name. Note that we don't need to escape strings
	//	here since PDO bindParam is already taking care to protect from SQL injections.
	$query = $intrapdo->prepare("SELECT * FROM users where username=:name");
	
	$query->bindParam(':name', $name, PDO::PARAM_STR, 256);
	$query->Execute();
	$row = $query->fetch();
	
	// If we have at least one row and that row is really for the same user the user exists.
	if($row !== false && $row['username']==$name)
	{		
		// crypt is really ill named function in php, it doesn't really crypt it does hash with salt a password.
		// This is the recommended way to verify a password. We pass to crypt both the user supplied password and the
		// expected hashed password. The hash contains also the hashing algorithm and the random salt that was 
		// used to hash the password. The function just hashes the supplied password using the salt and algorithm 
		// supplied. If this matches that hash in DB the password is the right one.
		if(time_constant_equal(crypt($password , $row['passwordHash']), $row['passwordHash']))
		{
			// We generate a random token. The token is 32 bytes long and, once hex encoded will be 64 chars.
			$token = bin2hex(openssl_random_pseudo_bytes(32));
		
			// We store the token in the user table. Note that this is a very simple approach that allows
			//	user to be authorized on only one device. A more complete solution would store several tokens
			//	with either a TTL or a device ID in a separate table, so user can authenticate on several devices.
			$query = $intrapdo->prepare("UPDATE users SET token=:token WHERE username=:name");
			$query->bindParam(':token', $token, PDO::PARAM_STR, 256);
			$query->bindParam(':name', $name, PDO::PARAM_STR, 256);
			$query->Execute();
			
			// This is the only reply given by this page.
			echo($token); 
		}
	}
	

	// Performs comparison of two strings in a time that depends only
	//	on the length of the first string and not on the content. Normal string
	//	comparison is optimized to stop comparing as soon as a difference is found.
	//	So for instance comparing "abcd1" to "abcd2" will take different time than
	//	comparing "abcd1" to "zbcd2". This is not safe in authentication as a remote
	//	attacker could, by timing the requests, gain insight of single correct 
	//	chars in the supplied data.
	// This function doesn't compare every single char but xors it to the expected one
	//	which yields a zero if the two are identical. The binary operation takes a 
	//	constant time regardless of the numbers involved and since every char is 
	//	processed regardless of result the execution time doesn't vary. Note that
	//  some implementations make use of a char comparison in place of the xor. Char
	//	comparison is often compiled anyway to a branched statement that takes different
	//	time to execute depending on the result, so that type of implementation gives
	//	away at least the amount of matching chars.
	function time_constant_equal($a, $b)
	{
		// Must be at least same length.
		$result = strlen($a) ^ strlen($b);
		
		// Go trough each char of $a and XOR it with the corresponding
		// char in $b, the result is zero only if they are equal.
		// We OR the comparison result with the final result since OR
		//	final result is zero only if both operands are zero.
		// In other words if only one of the XORs returns 1 the overall
		//	result is 1.
		for($i = 0; $i < strlen($a) && $i < strlen($b); $i++)
		{
			$result |= ord($a[$i]) ^ ord($b[$i]);
		}
		
		// Result is true (string equal) only if we got zeros at every step before.
		return $result === 0;
	}
?>
