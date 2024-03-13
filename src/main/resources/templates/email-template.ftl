<table cellspacing="0" cellpadding="0" align="center" width="100%" bgcolor="eeeeee">
	<tbody>
		<tr>
			<td>
			<table border="0" cellspacing="0" cellpadding="0" align="center" width="600" height="100%" bgcolor="4a4d4e" style="border-collapse: collapse; min-width: 600px;font-family:Arial;table-layout: fixed;">
	<tr>
		<td style="color: #CCCCCC;height: 24px;text-align: right;font-size: 9px;padding: 0 18px 0 18px;">View online version</td>
	</tr>
	<tr>
		<td style="height: 350px;">
			<img border="0" width="600" src="https://ndxdaas.com/ndx/documents/images/Newbury_Digital.png" alt="Email banner image">
		</td>
	</tr>
	<tr>
		<td valign="top" bgcolor="ffffff" style="padding: 16px 0 0 0;text-align: justify;">
			<p style="color: #4A4D4E;font-size: 24px;font-weight: bold;text-align: center;margin: 0 19px 0 19px;" >Error connecting to DMM</p>
			<br />
		</td>
	</tr>
	<tr>
		<td bgcolor="ffffff">
		</td>
	</tr>
	<tr>
		<td bgcolor="ffffff">
			<table align="center">
				<tbody>
				</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td  valign="top" bgcolor="ffffff" style="padding: 16px 19px 19px 19px;height: 100%;text-align: justify;" >
			<table>
				<tr>
					<td>
						<p style="color: #4A4D4E"><b>Dear Support team</b></p>
					</td>
				</tr>
				<tr>
					<td>
						<p style="color: #4A4D4E;padding:10px 0;">Applicaiton was not able to connect to DMM. </br>Please check the detail below and take the necessary action.</p>
					</td>
				</tr>
				<tr>
					<td>
						<p style="color: #E60000;font-size: 18px;font-family: Helvetica;font-weight: normal;padding:10px 0;">Error details</p>
					</td>
				</tr>
				<tr>
					<td>
						<table  valign="top" style="border: 1px solid black; border-collapse: collapse; width: 100%; font-family: Helvetica;">
							<tr valign="top">
								<td bgcolor="dddddd" style="color: #4A4D4E;width: 200px; font-weight: bold; padding: 10px 5px 10px 20px;">Service:</td>
								<td style="color: #4A4D4E;padding: 10px 5px 10px 20px;">${service}</td>
							</tr>
							<tr valign="top">
								<td bgcolor="dddddd" style="color: #4A4D4E;width: 200px; font-weight: bold; padding: 10px 5px 10px 20px;">Error:</td>
								<td style="color: #4A4D4E;padding: 10px 5px 10px 20px;">${error}</td>
							</tr>
							<tr valign="top">
                                <td bgcolor="dddddd" style="color: #4A4D4E;width: 200px; font-weight: bold; padding: 10px 5px 10px 20px;">Dmm:</td>
                                <td style="color: #4A4D4E;padding: 10px 5px 10px 20px;">${dmm}</td>
                            </tr>

                            <#if kafka_offset??>
							<tr valign="top">
								<td bgcolor="dddddd"style="color: #4A4D4E;width: 200px; font-weight: bold; padding: 10px 5px 10px 20px;">Kafka offset:</td>
								<td style="color: #4A4D4E;padding: 10px 5px 10px 20px;">${kafka_offset}</td>
							</tr>
							</#if>

							<#if kafka_messageKey??>
							<tr valign="top">
								<td bgcolor="dddddd" style="color: #4A4D4E; width: 200px; font-weight: bold; padding: 10px 5px 10px 20px;">Kafka message key:</td>
								<td style="color: #4A4D4E; padding: 10px 5px 10px 20px;">${kafka_messageKey}</td>
							</tr>
							</#if>

							<#if kafka_topic??>
							<tr valign="top">
								<td bgcolor="dddddd" style="color: #4A4D4E; width: 200px; font-weight: bold; padding: 10px 5px 10px 20px;">Kafka topic:</td>
								<td style="color: #4A4D4E; padding: 10px 5px 10px 20px;">${kafka_topic}</td>
							</tr>
							</#if>

							<tr valign="top">
								<td bgcolor="dddddd" style="color: #4A4D4E; width: 200px; font-weight: bold; padding: 10px 5px 10px 20px;">Payload:</td>
								<td style="color: #4A4D4E; padding: 10px 5px 10px 20px;">${payload}</td>
							</tr>
							<tr valign="top">
								<td bgcolor="f4f4f4" style="width: 200px; height: 30px; font-weight: bold; padding: 10px 5px 10px 20px;"></td>
								<td bgcolor="ffffff" style="padding: 10px 5px 10px 20px;"></td>
							</tr>
						</table>
					</td>
				</tr>
					<tr>
					<td></br>
					</td>
				</tr>
				<tr>
					<td>
						<p style="color: #4A4D4E;font-size: 12px; ">Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.</p>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</td>
		</tr>
	</tbody>
</table>