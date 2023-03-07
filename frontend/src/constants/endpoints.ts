export const LISTING = "/listings"

export const isAuthenticationNeeded = (endPoint: string) => {
    const autheticatedURLS = [LISTING]
    return autheticatedURLS.includes(endPoint)
}