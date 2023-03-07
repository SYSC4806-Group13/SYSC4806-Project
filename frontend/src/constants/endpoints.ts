export const LISTING = "/listing"

export const isAuthenticationNeeded = (endPoint: string) => {
    const autheticatedURLS = [LISTING]
    return autheticatedURLS.includes(endPoint)
}