namespace java org


service NetworkConnectorService
{
        string ping(),
        string sendQuery(1:string query),
}