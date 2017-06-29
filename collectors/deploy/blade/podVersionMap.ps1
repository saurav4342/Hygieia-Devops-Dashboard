
cls ### clear screen

##Write-Host "PODNAME  ----- VERSION ----- DEPLOYED ON"
##Write-Host "======================================================"

foreach($line in Get-Content C:\Users\nayaksau\Documents\script\file.txt) {  ## looping SERVER-POD mapping file 
$server,$pod=$line.split("-");  ## separating server and pod and assigning to variable 
                                                $pod=$pod -replace "`n|`r" ## removing new-line character in the $pod variable

                                                $webConfig = get-content -path \\$server\d$\ADP\ezLM\net2\ezLaborManagerNet\web.config ## getting web.config from each server and assigning to a variable 
                                                $webConfigXml = New-Object XML  ## creating an XML object
                                                $webConfigXml.LoadXml($webConfig) ## load the text file content into XML object

$version=$webConfigXml.configuration.appSettings.add | where {$_.Key -eq 'JSCacheValue'} ## getting JSCacheValue as key value pair
$date=$webConfigXml.configuration.appSettings.add | where {$_.Key -eq 'AppReleaseDate'}  ## getting AppReleaseDate as key value pair
                           
$versionnumber=$version.value  ## capturing value of key JSCacheValue
$AppReleaseDate=$date.value    ## capturing value of key AppReleaseDate

Write-Host "$pod  --- $versionnumber --- $AppReleaseDate" ### display the versions

}
