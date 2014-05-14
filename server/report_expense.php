<?php
/* report_expense.php is part of ExpensesReporter and is responsible to
 *   accept requests to sync expenses. Currently we support only POST
 *	 to add an expense.
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
	$request_id =(isset($_REQUEST['_id']))?$_REQUEST['_id']:0;
	$description =(isset($_REQUEST['description']))?$_REQUEST['description']:"";
	$currency =(isset($_REQUEST['currency']))?$_REQUEST['currency']:"";
	$amount =(isset($_REQUEST['amount']))?$_REQUEST['amount']:exit;
	$destination =(isset($_REQUEST['destination']))?$_REQUEST['destination']:exit;
	$source =(isset($_REQUEST['source']))?$_REQUEST['source']:exit;
	$timestamp =(isset($_REQUEST['timestamp']))?$_REQUEST['timestamp']:exit;
				
	// Get a database connection
	require_once("pdo_intra.php");
	
	// Insert the expense in database. Note that we don't need to escape strings
	//	here since PDO bindParam is already taking care to protect from SQL injections.
	$query = $intrapdo->prepare("INSERT INTO ex_expenses SET description=:description,
			  											   amount=:amount,
														   transactionto=:destination,
														   transactionfrom=:source,
														   raw_id=:request_id");
	
	$query->bindParam(':description', $description, PDO::PARAM_STR, 256);
	$query->bindParam(':amount', $amount, PDO::PARAM_STR, 256);
	$query->bindParam(':destination', $destination, PDO::PARAM_STR, 256);
	$query->bindParam(':source', $source, PDO::PARAM_STR, 256);
	$query->bindParam(':request_id', $request_id, PDO::PARAM_INT);
	$query->Execute();
?>
